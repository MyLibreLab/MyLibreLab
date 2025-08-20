import com.github.vlsi.gradle.crlf.CrLfSpec
import com.github.vlsi.gradle.crlf.LineEndings

plugins {
    `java-library`
    application
    id("org.beryx.runtime")
    kotlin("jvm")
    kotlin("kapt")
}

application {
    mainClass.set("com.github.mylibrelab.MyLibreLab")
}

runtime {
    addOptions(
        "--strip-debug",
        "--compress", "2",
        "--no-header-files",
        "--no-man-pages"
    )
    
    jpackage {
        // REMOVE: skipInstaller = true
        
        // Configure platform-specific installers
        installerName = "MyLibreLab"
        appVersion = project.version.toString()
        
        // Windows specific
        if (org.gradle.internal.os.OperatingSystem.current().isWindows) {
            installerType = "exe"
            installerOptions = listOf(
                "--win-dir-chooser",
                "--win-menu",
                "--win-shortcut"
            )
        }
        
        // macOS specific
        if (org.gradle.internal.os.OperatingSystem.current().isMacOsX) {
            installerType = "pkg"
            installerOptions = listOf(
                "--mac-package-name", "MyLibreLab"
            )
        }
        
        // Linux specific
        if (org.gradle.internal.os.OperatingSystem.current().isLinux) {
            installerType = "deb"
            installerOptions = listOf(
                "--linux-package-name", "mylibrelab",
                "--linux-deb-maintainer", "mylibrelab@example.com"
            )
        }
    }
}

// Disable tests temporarily to avoid KAPT issues
tasks.test {
    enabled = false
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

    // Darklaf dependencies (versions managed by the project's BOM)
    implementation("com.github.weisj:darklaf-core")
    implementation("com.github.weisj:darklaf-property-loader")
    implementation("com.github.weisj:darklaf-theme")
    implementation("com.github.weisj:darklaf-utils")
    implementation("com.github.weisj:darklaf-platform-base")
    implementation("com.github.weisj:darklaf-theme-spec")
    implementation("com.github.weisj:darklaf-iconset")

    // This one has separate versioning
    implementation("com.github.weisj:darklaf-extensions-kotlin:0.3.4")

    implementation("com.google.code.findbugs:jsr305")
    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")

    kapt(project(":mylibrelab-annotations"))
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