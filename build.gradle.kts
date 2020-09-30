plugins {
    `java-library`
    application
    id("org.beryx.runtime")
}

val String.v: String get() = rootProject.extra["$this.version"] as String
val projectVersion = "MyLibreLab".v

dependencies {
    implementation("com.google.guava:guava:28.2-jre")
    implementation("javax.vecmath:vecmath")
    implementation("eu.hansolo:SteelSeries")
    implementation("org.pushing-pixels:trident")
    implementation("org.json:json")
    implementation("net.java.dev.jna:jna-platform")
    implementation("org.scream3r:jssc")
    implementation("org.bidib.jbidib:bidib-rxtx-binaries")

    implementation("com.pi4j:pi4j-core")
    implementation("com.pi4j:pi4j-device")
    implementation("com.pi4j:pi4j-gpio-extension")
    implementation("com.pi4j:pi4j-service")

    implementation("javax.xml.bind:jaxb-api")
    implementation("com.sun.xml.bind:jaxb-core")
    implementation("com.sun.xml.bind:jaxb-impl")

    implementation("org.tinylog:tinylog-api")
    implementation("org.tinylog:tinylog-impl")

    implementation("org.netbeans.external:AbsoluteLayout")
    implementation("org.swinglabs:swing-layout")

    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")


    // implementation("org.netbeans.external:swing-layout-1.0.4:RELEASE68")
    // implementation fileTree(dir: "distribution/lib", include: ["*.jar])
    // implementation fileTree(dir: "distribution/lib_win_64", include: ["*.jar"])
    // implementation fileTree(dir: "jssc", include: ["*.jar"])
    // implementation fileTree(dir: "pi4j-1.0", include: ["*.jar"])
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

allprojects {
    version = projectVersion

    repositories {
        mavenCentral()
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
    }
}
