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

    // JUnit 5 testing dependencies
    testImplementation(platform("org.junit:junit-bom:5.10.3"))
    testImplementation("org.junit.jupiter:junit-jupiter")        // api + params
    testRuntimeOnly("org.junit.platform:junit-platform-launcher") // Gradle 7.x runner
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
    systemProperty("java.awt.headless", "true") // avoids UI pops in tests
}

/**
 * Helper function to create platform-specific launchers
 */
fun createLaunchers(outputDir: File, os: OperatingSystem) {
    // Windows launcher
    val winBat = File(outputDir, "MyLibreLab.bat")
    winBat.writeText("""
        @echo off
        setlocal EnableDelayedExpansion

        REM Get the directory of this script
        set "APP_HOME=%~dp0"
        if "%APP_HOME:~-1%"=="\" set "APP_HOME=%APP_HOME:~0,-1%"

        REM Check if runtime exists
        if not exist "%APP_HOME%\runtime\bin\java.exe" (
            echo Error: Java runtime not found at %APP_HOME%\runtime\bin\java.exe
            echo Please ensure the portable app was created correctly.
            pause
            exit /b 1
        )

        REM Check if lib directory exists
        if not exist "%APP_HOME%\lib" (
            echo Error: Library directory not found at %APP_HOME%\lib
            echo Please ensure the portable app was created correctly.
            pause
            exit /b 1
        )

        echo Starting MyLibreLab...

        REM Launch MyLibreLab
        "%APP_HOME%\runtime\bin\java.exe" ^
        -Dswing.defaultlaf=com.github.weisj.darklaf.DarkLaf ^
        -Ddarklaf.theme=one_dark ^
        -Ddarklaf.useBufferedRepaintManager=true ^
        --add-exports=java.desktop/com.sun.java.swing=ALL-UNNAMED ^
        -Dmylibrelab.elements="%APP_HOME%\elements" ^
        -Dmylibrelab.home="%APP_HOME%" ^
        -cp "%APP_HOME%\lib\*" com.github.mylibrelab.MyLibreLab %*

        set EXIT_CODE=%ERRORLEVEL%
        if %EXIT_CODE% neq 0 (
            echo.
            echo MyLibreLab exited with error code %EXIT_CODE%.
            pause
        )
        exit /b %EXIT_CODE%
    """.trimIndent())

    // Unix launcher (Linux/macOS)
    val unixSh = File(outputDir, "MyLibreLab.sh")
    unixSh.writeText("""
        #!/usr/bin/env bash
        set -e

        # Get the directory of this script
        DIR="$(cd "$(dirname "${'$'}0")" && pwd)"

        # Check if runtime exists
        if [ ! -f "${'$'}DIR/runtime/bin/java" ]; then
            echo "Error: Java runtime not found at ${'$'}DIR/runtime/bin/java"
            echo "Please ensure the portable app was created correctly."
            exit 1
        fi

        # Check if lib directory exists
        if [ ! -d "${'$'}DIR/lib" ]; then
            echo "Error: Library directory not found at ${'$'}DIR/lib"
            echo "Please ensure the portable app was created correctly."
            exit 1
        fi

        echo "Starting MyLibreLab..."

        # Launch MyLibreLab
        exec "${'$'}DIR/runtime/bin/java" \
        -Dswing.defaultlaf=com.github.weisj.darklaf.DarkLaf \
        -Ddarklaf.theme=one_dark \
        -Ddarklaf.useBufferedRepaintManager=true \
        --add-exports=java.desktop/com.sun.java.swing=ALL-UNNAMED \
        -Dmylibrelab.elements="${'$'}DIR/elements" \
        -Dmylibrelab.home="${'$'}DIR" \
        -cp "${'$'}DIR/lib/*" com.github.mylibrelab.MyLibreLab "${'$'}@"
    """.trimIndent())
    unixSh.setExecutable(true)

    // macOS convenience launcher
    val macCmd = File(outputDir, "MyLibreLab.command")
    macCmd.writeText(unixSh.readText())
    macCmd.setExecutable(true)

    println("✅ Created launchers: MyLibreLab.bat, MyLibreLab.sh, MyLibreLab.command")
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

        println("🚀 Creating portable MyLibreLab application...")
        println("📍 Install directory: ${installDir.absolutePath}")
        println("📍 Output directory: ${outputDir.absolutePath}")

        // Clean and create output directory
        if (outputDir.exists()) {
            println("🧹 Cleaning existing output directory...")
            outputDir.deleteRecursively()
        }
        outputDir.mkdirs()

        // Validate Java installation
        val javaHome = System.getProperty("java.home")
        val jlinkExe = file("$javaHome/bin/jlink${if (os.isWindows) ".exe" else ""}")
        val jmodsDir = file("$javaHome/jmods")

        println("☕ Java Home: $javaHome")
        println("🔗 jlink executable: ${jlinkExe.absolutePath}")
        println("📦 jmods directory: ${jmodsDir.absolutePath}")

        require(jlinkExe.exists()) {
            "jlink not found at: ${jlinkExe.absolutePath}. " +
            "Ensure you're using a full JDK (not just JRE). " +
            "Current java.home: $javaHome"
        }
        require(jmodsDir.exists()) {
            "jmods directory not found at: ${jmodsDir.absolutePath}. " +
            "Ensure you're using a full JDK distribution."
        }

        // Verify installDist was successful
        require(installDir.exists()) {
            "Install directory not found at: ${installDir.absolutePath}. " +
            "Please run 'gradlew installDist' first."
        }

        val libDir = File(installDir, "lib")
        require(libDir.exists() && libDir.listFiles()?.isNotEmpty() == true) {
            "No JAR files found in ${libDir.absolutePath}. " +
            "Please ensure 'gradlew installDist' completed successfully."
        }

        println("✅ Validation passed. Creating custom JRE with jlink...")

        // Create minimal JRE with required modules
        val modules = listOf(
            "java.base",
            "java.desktop",
            "java.logging",
            "java.xml",
            "java.datatransfer",
            "java.scripting",
            "java.sql",
            "java.compiler",
            "jdk.compiler",
            "jdk.zipfs",
            "jdk.localedata",
            "jdk.unsupported"
        ).joinToString(",")

        val runtimeDir = File(outputDir, "runtime")

        try {
            exec {
                commandLine(
                    jlinkExe.absolutePath,
                    "--add-modules", modules,
                    "--output", runtimeDir.absolutePath,
                    "--no-header-files",
                    "--no-man-pages",
                    "--compress=2",
                    "--strip-debug"
                )
            }
            println("✅ Custom JRE created successfully")
        } catch (e: Exception) {
            throw RuntimeException("Failed to create custom JRE: ${e.message}", e)
        }

        // Copy application files from installDist
        println("📂 Copying application files...")
        copy {
            from(installDir)
            into(outputDir)
        }

        // Include elements directory if it exists
        val elementsSrc = project.rootDir.resolve("elements")
        if (elementsSrc.exists()) {
            println("📦 Including elements directory...")
            copy {
                from(elementsSrc)
                into(File(outputDir, "elements"))
            }
            println("✅ Elements directory included")
        } else {
            println("⚠️ elements/ directory not found - app will work but without legacy elements")
            // Create empty elements directory to avoid runtime errors
            File(outputDir, "elements").mkdirs()
        }

        // Create platform-specific launchers
        println("🚀 Creating platform launchers...")
        createLaunchers(outputDir, os)

        // Verify the final structure
        println("📁 Portable app structure:")
        outputDir.listFiles()?.sortedBy { it.name }?.forEach { file ->
            val icon = if (file.isDirectory()) "📁" else "📄"
            val size = if (file.isDirectory()) {
                val fileCount = file.listFiles()?.size ?: 0
                "($fileCount items)"
            } else {
                "(${file.length() / 1024}KB)"
            }
            println("  $icon ${file.name} $size")
        }

        // Verify runtime was created correctly
        val javaExe = File(runtimeDir, "bin/${if (os.isWindows) "java.exe" else "java"}")
        if (javaExe.exists()) {
            println("✅ Java runtime verified at: ${javaExe.absolutePath}")
        } else {
            throw RuntimeException("Java executable not found in custom runtime")
        }

        // Verify libs were copied
        val libDirOutput = File(outputDir, "lib")
        val jarCount = libDirOutput.listFiles { _, name -> name.endsWith(".jar") }?.size ?: 0
        println("✅ $jarCount JAR files copied to lib directory")

        println("🎉 Portable app created successfully at: ${outputDir.absolutePath}")
        println("🚀 You can now run MyLibreLab using:")
        if (os.isWindows) {
            println("   ${outputDir.absolutePath}\\MyLibreLab.bat")
        } else {
            println("   ${outputDir.absolutePath}/MyLibreLab.sh")
        }
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
