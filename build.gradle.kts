import com.github.autostyle.generic.DefaultCopyrightStyle
import com.github.autostyle.gradle.BaseFormatExtension
import com.github.vlsi.gradle.properties.dsl.props

plugins {
    `java-library`
    application
    id("com.github.autostyle")
    id("org.sonarqube")
    id("com.github.vlsi.gradle-extensions")
    id("org.beryx.runtime")
}

val String.v: String get() = rootProject.extra["$this.version"] as String
val projectVersion = "MyLibreLab".v

val enableMavenLocal by props()
val skipAutostyle by props()

dependencies {
    implementation("org.json:json")
    implementation("org.scream3r:jssc")

    implementation("javax.xml.bind:jaxb-api")
    runtimeOnly("com.sun.xml.bind:jaxb-core")
    runtimeOnly("com.sun.xml.bind:jaxb-impl")

    implementation("org.tinylog:tinylog-api")
    runtimeOnly("org.tinylog:tinylog-impl")

    implementation("org.netbeans.external:AbsoluteLayout")
    implementation("org.swinglabs:swing-layout")

    implementation("com.google.code.findbugs:jsr305")
    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")

    /* Currently unused dependencies. Those need further investigation whether they are needed for the elements
     * at runtime.
    implementation("com.google.guava:guava:28.2-jre")
    implementation("javax.vecmath:vecmath")
    implementation("eu.hansolo:SteelSeries")
    implementation("org.pushing-pixels:trident")
    implementation("net.java.dev.jna:jna-platform")
    implementation("org.bidib.jbidib:bidib-rxtx-binaries")

    runtimeOnly("com.pi4j:pi4j-core")
    runtimeOnly("com.pi4j:pi4j-device")
    runtimeOnly("com.pi4j:pi4j-gpio-extension")
    runtimeOnly("com.pi4j:pi4j-service")
    */

    /*
    implementation fileTree(dir: "distribution/lib", include: ["*.jar])
    implementation fileTree(dir: "distribution/lib_win_64", include: ["*.jar"])
    implementation fileTree(dir: "jssc", include: ["*.jar"])
    implementation fileTree(dir: "pi4j-1.0", include: ["*.jar"])
    */
}

application {
    mainClassName = "VisualLogic.FrameMain"
}

runtime {
    addOptions(
        "--strip-debug",
        "--compress", "2",
        "--no-header-files",
        "--no-man-pages"
    )
}

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

val sonarBranchName by props("")
sonarqube {
    properties {
        if (sonarBranchName.isNotEmpty()) {
            property("sonar.pullrequest.branch", sonarBranchName)
        }
    }
}

allprojects {
    version = projectVersion

    repositories {
        if (enableMavenLocal) {
            mavenLocal()
        }
        mavenCentral()
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
            "api"(platform(project(":mylibrelab-dependencies-bom")))
            "annotationProcessor"(platform(project(":mylibrelab-dependencies-bom")))
        }

        configure<JavaPluginExtension> {
            sourceCompatibility = JavaVersion.VERSION_11
            targetCompatibility = JavaVersion.VERSION_11
        }

        tasks {
            withType<JavaCompile> {
                options.encoding = "UTF-8"
            }
            withType<Test> {
                // Use junit platform for unit tests
                useJUnitPlatform()
            }
        }
        if (!skipAutostyle) {
            autostyle {
                java {
                    importOrder("java", "javax", "org", "com", "")
                    removeUnusedImports()
                    eclipse {
                        configFile("${project.rootDir}/config/style.eclipseformat.xml")
                    }
                    license()
                }
            }
        }
    }
}
