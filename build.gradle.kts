import com.github.autostyle.generic.DefaultCopyrightStyle
import com.github.autostyle.gradle.BaseFormatExtension
import com.github.vlsi.gradle.crlf.CrLfSpec
import com.github.vlsi.gradle.crlf.LineEndings
import com.github.vlsi.gradle.properties.dsl.props
import name.remal.gradle_plugins.plugins.code_quality.sonar.SonarLintExtension
import org.jetbrains.kotlin.gradle.plugin.KaptExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("com.github.autostyle")
    id("com.github.vlsi.crlf")
    id("com.github.vlsi.gradle-extensions")
    id("org.sonarqube")
    id("org.beryx.runtime")
    id("name.remal.sonarlint") apply false
    kotlin("jvm") apply false
    kotlin("kapt") apply false
}

val String.v: String get() = rootProject.extra["$this.version"] as String
val projectVersion = "MyLibreLab".v

val enableMavenLocal by props()
val skipAutostyle by props()
val skipSonarlint by props()

println("Building: MyLibreLab $projectVersion")
println("     JDK: " + System.getProperty("java.home"))

fun BaseFormatExtension.license() {
    licenseHeader(File("${project.rootDir}/config/LICENSE_HEADER.txt").readText()) {
        copyrightStyle("bat", DefaultCopyrightStyle.REM)
        copyrightStyle("cmd", DefaultCopyrightStyle.REM)
        addBlankLineAfter.set(true)
    }
    trimTrailingWhitespace()
    endWithNewline()
}

fun BaseFormatExtension.configFilter(init: PatternFilterable.() -> Unit) {
    filter {
        // Autostyle does not support gitignore yet https://github.com/autostyle/autostyle/issues/13
        exclude("out/**")
        if (project == rootProject) {
            exclude("gradlew*", "gradle/**")
        } else {
            exclude("bin/**")
        }
        init()
    }
}

allprojects {
    version = projectVersion

    repositories {
        if (enableMavenLocal) {
            mavenLocal()
        }
        mavenCentral()
        maven {
            url = uri("https://oss.sonatype.org/content/repositories/snapshots/")
        }
    }

    configurations.all {
        resolutionStrategy.cacheChangingModulesFor(0, "seconds")
    }

    if (!skipSonarlint) {
        apply(plugin = "name.remal.sonarlint")
        val allowSonarlintFailures by props()
        configure<SonarLintExtension> {
            isIgnoreFailures = allowSonarlintFailures
            excludes {
                sources(listOf(
                    "**/BasisStatus/",
                    "**/codeeditor/",
                    "**/create_new_group/",
                    "**/CustomColorPicker/",
                    "**/de/myopenlab/update/",
                    "**/MyGraph/",
                    "**/MyParser/",
                    "**/ParserCode/",
                    "**/Peditor/",
                    "**/projectfolder/",
                    "**/SimpleFileSystem/",
                    "**/SimulatorSocket/",
                    "**/VisualLogic/",
                    "**/ziputils/"
                ))
                // Ignore [Fields in a "Serializable" class should either be transient or serializable]
                // We do not plan to use serialization, but most swing components declare that they are.
                message("java:S1948")
                // Ignore [Inheritance tree of classes should not be too deep]
                // Extending JComponent will break this rule.
                message("java:S110")
                // Some classes benefit from more descriptive generic type parameters.
                message("java:S119")
            }
        }
    }

    if (!skipAutostyle) {
        apply(plugin = "com.github.autostyle")

        autostyle {
            kotlinGradle {
                ktlint()
            }
            format("properties") {
                configFilter {
                    include("**/*.properties")
                    exclude("**/gradle.properties")
                }
                license()
            }
            format("configs") {
                configFilter {
                    include("**/*.sh", "**/*.bsh", "**/*.cmd", "**/*.bat")
                    exclude("**/*.xsd", "**/*.xsl", "**/*.xml")
                    exclude("**/*.yml")
                    exclude("**/*.eclipseformat.xml")
                }
                license()
            }
            format("markdown") {
                filter.include("**/*.md")
                endWithNewline()
            }
        }
        plugins.withType<JavaBasePlugin> {
            autostyle {
                kotlin {
                    ktlint(version = "0.39.0") {
                        userData(mapOf("disabled_rules" to "no-wildcard-imports"))
                    }
                    license()
                }
            }
        }
        plugins.withType<JavaPlugin> {
            autostyle {
                java {
                    importOrder("java", "javax", "org", "com", "")
                    addStep(FixedRemoveUnusedImportsStep())

                    eclipse {
                        configFile("${project.rootDir}/config/style.eclipseformat.xml")
                    }
                    license()
                }
            }
        }
    }

    tasks.withType<AbstractArchiveTask>().configureEach {
        // Ensure builds are reproducible
        isPreserveFileTimestamps = false
        isReproducibleFileOrder = true
        dirMode = "775".toInt(8)
        fileMode = "664".toInt(8)
    }

    plugins.withType<JavaLibraryPlugin> {
        dependencies {
            val bom = platform(project(":mylibrelab-dependencies-bom"))
            "api"(bom)
            "annotationProcessor"(bom)
        }
    }

    extensions.findByType(KaptExtension::class)?.run {
        dependencies {
            "kapt"(platform(project(":mylibrelab-dependencies-bom")))
        }
        includeCompileClasspath = false
    }

    tasks.withType<KotlinCompile>().configureEach {
        kotlinOptions {
            jvmTarget = "15"
            freeCompilerArgs = listOf(
                "-Xjvm-default=enable",
                "-Xopt-in=kotlin.RequiresOptIn"
            )
        }
    }

    plugins.withType<JavaPlugin> {

        configure<JavaPluginExtension> {
            sourceCompatibility = JavaVersion.VERSION_15
            targetCompatibility = JavaVersion.VERSION_15
        }

        tasks {
            withType<JavaCompile> {
                options.encoding = "UTF-8"
            }
            withType<Test> {
                // Use junit platform for unit tests
                useJUnitPlatform()
            }
            withType<Jar>().configureEach {
                manifest {
                    attributes["Bundle-License"] = "GPL-3.0"
                    attributes["Implementation-Title"] = project.name
                    attributes["Implementation-Version"] = project.version
                    attributes["Specification-Vendor"] = "MyLibreLab"
                    attributes["Specification-Version"] = project.version
                    attributes["Specification-Title"] = "MyLibreLab"
                    attributes["Implementation-Vendor"] = "MyLibreLab"
                    attributes["Implementation-Vendor-Id"] = "com.github.mylibrelab"
                }

                CrLfSpec(LineEndings.LF).run {
                    into("META-INF") {
                        filteringCharset = "UTF-8"
                        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
                        // This includes either project-specific license, or a default one
                        if (file("$projectDir/LICENSE").exists()) {
                            textFrom("$projectDir/LICENSE")
                        } else {
                            textFrom("$rootDir/LICENSE")
                        }
                    }
                }
            }
        }
    }
}
