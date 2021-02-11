import com.github.vlsi.gradle.crlf.CrLfSpec
import com.github.vlsi.gradle.crlf.LineEndings

plugins {
    `java-library`
    application
    id("org.beryx.runtime")
    kotlin("jvm")
    kotlin("kapt")
}

dependencies {
    implementation(project(":mylibrelab-settings-api"))
    implementation(project(":mylibrelab-service-manager"))
    implementation(project(":mylibrelab-util"))

    implementation("org.json:json")
    implementation("org.scream3r:jssc")

    implementation("javax.xml.bind:jaxb-api")
    runtimeOnly("com.sun.xml.bind:jaxb-core")
    runtimeOnly("com.sun.xml.bind:jaxb-impl")

    implementation("org.tinylog:tinylog-api")
    runtimeOnly("org.tinylog:tinylog-impl")

    implementation("org.netbeans.external:AbsoluteLayout")
    implementation("org.swinglabs:swing-layout")
    implementation("com.miglayout:miglayout-swing")
    implementation("com.github.weisj:darklaf-core")
    implementation("com.github.weisj:darklaf-property-loader")
    implementation("com.github.weisj:darklaf-extensions-kotlin")

    implementation("com.google.code.findbugs:jsr305")
    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")

    implementation("net.java.dev.jna:jna:5.7.0")
    implementation("org.openmuc:jrxtx:1.0.1")
    implementation("org.bidib.jbidib.eu.hansolo:SteelSeries:3.9.31.2")
    implementation("guru.nidi.com.kitfox:svgSalamander:1.1.3")
//    implementation("com.barchart.kitfox:kitfox-svg-core:1.0.0-build001")
//    implementation("com.barchart.kitfox:kitfox-svg-editor:1.0.0-build001")
//    implementation("com.weblookandfeel:svg-salamander:1.1.2.2")
    implementation("org.jfree:jfreechart:1.5.2")

    kapt(project(":mylibrelab-annotations"))

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
    mainClassName = "com.github.mylibrelab.MyLibreLab"
    mainClass.set(mainClassName)
}

runtime {
    addOptions(
        "--strip-debug",
        "--compress", "2",
        "--no-header-files",
        "--no-man-pages"
    )
}

fun Jar.includeLicenses() {
    CrLfSpec(LineEndings.LF).run {
        into("META-INF") {
            filteringCharset = "UTF-8"
            textFrom("$rootDir/licenses/INTELLIJ_LICENSE.txt")
            textFrom("$rootDir/licenses/INTELLIJ_NOTICE.txt")
            textFrom("$rootDir/licenses/MIGLAYOUT_LICENSE.txt")
        }
    }
}

tasks.jar {
    includeLicenses()
}
