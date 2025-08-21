import com.github.vlsi.gradle.crlf.CrLfSpec
import com.github.vlsi.gradle.crlf.LineEndings

plugins {
    `java-library`
    application
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
    mainClass.set("com.github.mylibrelab.MyLibreLab")
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

tasks.register("createPortableApp") {
    dependsOn("installDist")

    doLast {
        val javaHome = System.getProperty("java.home")
        val buildDir = layout.buildDirectory.get().asFile
        val installDir = File(buildDir, "install/mylibrelab-application")
        val outputDir = File(buildDir, "portable")

        // Clean output directory
        if (outputDir.exists()) {
            outputDir.deleteRecursively()
        }
        outputDir.mkdirs()

        // Create custom JRE with jlink - ADD jdk.compiler module
        val jlinkCmd = listOf(
            "$javaHome/bin/jlink",
            "--module-path", "$javaHome/jmods",
            "--add-modules", "java.base,java.desktop,java.logging,java.management,java.naming,java.security.sasl,java.xml,jdk.unsupported,jdk.compiler",
            "--output", File(outputDir, "runtime").absolutePath,
            "--no-header-files",
            "--no-man-pages",
            "--compress=2",
            "--strip-debug"
        )

        exec {
            commandLine(jlinkCmd)
        }

        // Copy application files
        copy {
            from(installDir)
            into(outputDir)
        }

        // Create Windows launcher
        val windowsLauncher = File(outputDir, "MyLibreLab.bat")
        windowsLauncher.writeText("""
            @echo off
            "%~dp0runtime\bin\java.exe" -cp "%~dp0lib\*" com.github.mylibrelab.MyLibreLab %*
        """.trimIndent())

        // Create Unix launcher
        val unixLauncher = File(outputDir, "MyLibreLab.sh")
        unixLauncher.writeText("""
            #!/bin/bash
            DIR="${'$'}(cd "${'$'}(dirname "${'$'}{BASH_SOURCE[0]}")" && pwd)"
            "${'$'}DIR/runtime/bin/java" -cp "${'$'}DIR/lib/*" com.github.mylibrelab.MyLibreLab "${'$'}@"
        """.trimIndent())
        unixLauncher.setExecutable(true)

        println("Portable app created in: ${outputDir.absolutePath}")
    }
}
