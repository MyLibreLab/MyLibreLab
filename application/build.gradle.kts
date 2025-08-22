import org.gradle.internal.os.OperatingSystem
import java.io.File

plugins {
    `java-library`
    application
    kotlin("jvm")
    kotlin("kapt")

    // Phase 2 (optional): present but harmless unless you run native tasks
    id("org.graalvm.buildtools.native") version "0.10.2"
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

application {
    // Main entry point
    mainClass.set("com.github.mylibrelab.MyLibreLab")
}

dependencies {
    // Local modules
    implementation(project(":mylibrelab-settings-api"))
    implementation(project(":mylibrelab-service-manager"))
    implementation(project(":mylibrelab-util"))

    // Use the internal BOM for versions
    implementation(platform(project(":mylibrelab-dependencies-bom")))

    // UI / logging / comms (versions come from BOM)
    implementation("com.github.weisj:darklaf-core")
    implementation("com.github.weisj:darklaf-property-loader")
    implementation("com.github.weisj:darklaf-extensions-kotlin")

    implementation("org.swinglabs:swing-layout")
    implementation("com.miglayout:miglayout-swing")
    implementation("org.netbeans.external:AbsoluteLayout")

    implementation("org.scream3r:jssc")

    implementation("javax.xml.bind:jaxb-api")
    runtimeOnly("com.sun.xml.bind:jaxb-core")
    runtimeOnly("com.sun.xml.bind:jaxb-impl")

    implementation("org.tinylog:tinylog-api")
    runtimeOnly("org.tinylog:tinylog-impl")

    implementation("org.json:json")                   // version comes from BOM
    compileOnly("com.google.code.findbugs:jsr305")    // version comes from BOM
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
}

/**
 * createPortableApp
 *
 * Produces a relocatable application directory under:
 *   application/build/portable/
 * Structure:
 *   - lib/       (all runtime jars)
 *   - runtime/   (custom JRE produced by jlink from the current JDK)
 *   - MyLibreLab.bat / MyLibreLab.sh / MyLibreLab.command launchers
 */
val createPortableApp by tasks.registering {
    group = "distribution"
    description = "Create a portable app with a custom runtime and launchers"
    dependsOn(tasks.named("installDist"))

    doLast {
        val os = OperatingSystem.current()
        val installDir = layout.buildDirectory.dir("install/${project.name}").get().asFile
        val outputDir = layout.buildDirectory.dir("portable").get().asFile

        // Fresh output
        if (outputDir.exists()) outputDir.deleteRecursively()
        outputDir.mkdirs()

        // Build custom JRE via jlink from the current JDK
        val javaHome = System.getProperty("java.home")
        val jlinkExe = file("$javaHome/bin/jlink" + if (os.isWindows) ".exe" else "")
        val jmodsDir = file("$javaHome/jmods")
        require(jlinkExe.exists()) { "jlink not found at: $jlinkExe" }
        require(jmodsDir.exists()) { "jmods not found at: $jmodsDir" }

        val modules = listOf(
            "java.base",
            "java.desktop",
            "java.logging",
            "java.xml",
            "java.datatransfer",
            "java.scripting",
            "java.sql",
            // ✅ required for javax.tools.* API + compiler implementation
            "java.compiler",
            "jdk.compiler",
            // nice-to-have for jar/zip NIO and locale data
            "jdk.zipfs",
            "jdk.localedata",
            "jdk.unsupported"
        ).joinToString(",")


        exec {
            commandLine(
                jlinkExe.absolutePath,
                "--add-modules", modules,
                "--output", File(outputDir, "runtime").absolutePath,
                "--no-header-files",
                "--no-man-pages",
                "--compress=2",
                "--strip-debug"
            )
        }

        // Copy application files from installDist into portable root
        copy {
            from(installDir)
            into(outputDir)
        }

        // ✅ Include elements/ for a self-contained portable bundle
        val elementsSrc = project.rootDir.resolve("elements")
        if (elementsSrc.exists()) {
            copy {
                from(elementsSrc)
                into(File(outputDir, "elements"))
            }
            println("✅ Copied elements to portable bundle.")
        } else {
            println("ℹ️ elements/ directory not found in repo; skipping.")
        }


        // Windows launcher
        val winBat = File(outputDir, "MyLibreLab.bat")
        winBat.writeText(
            """
            @echo off
            setlocal
            set "APP_HOME=%~dp0"
            "%APP_HOME%runtime\bin\java.exe" ^
            -Dswing.defaultlaf=com.github.weisj.darklaf.DarkLaf ^
            -Ddarklaf.theme=one_dark ^
            -Ddarklaf.useBufferedRepaintManager=true ^
            --add-exports=java.desktop/com.sun.java.swing=ALL-UNNAMED ^
            -Dmylibrelab.elements="%APP_HOME%elements" ^
            -cp "%APP_HOME%lib\*" com.github.mylibrelab.MyLibreLab %*
            """.trimIndent()
        )


        // Unix launcher
        val unixSh = File(outputDir, "MyLibreLab.sh")
        unixSh.writeText(
            """
            #!/usr/bin/env bash
            DIR="$(cd "$(dirname "${'$'}0")" && pwd)"
            exec "${'$'}DIR/runtime/bin/java" \
            -Dswing.defaultlaf=com.github.weisj.darklaf.DarkLaf \
            -Ddarklaf.theme=one_dark \
            -Ddarklaf.useBufferedRepaintManager=true \
            --add-exports=java.desktop/com.sun.java.swing=ALL-UNNAMED \
            -Dmylibrelab.elements="${'$'}DIR/elements" \
            -cp "${'$'}DIR/lib/*" com.github.mylibrelab.MyLibreLab "${'$'}@"
            """.trimIndent()
        )
        unixSh.setExecutable(true)

        // macOS convenience launcher
        val macCmd = File(outputDir, "MyLibreLab.command")
        macCmd.writeText(unixSh.readText())
        macCmd.setExecutable(true)



        println("✅ Portable app created at: ${outputDir.absolutePath}")
    }
}

/* ---------------------------
   Phase 2: GraalVM Native Image (optional)
   --------------------------- */
graalvmNative {
    binaries {
        named("main") {
            imageName.set("MyLibreLab")
            // Smaller binary; requires proper reflection/resources config if used
            buildArgs.add("--no-fallback")
            resources.autodetect()
        }
    }
}
