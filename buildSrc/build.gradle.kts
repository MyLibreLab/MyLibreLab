apply(from= "../gradle/loadProps.gradle.kts")

plugins {
    `kotlin-dsl`
}

val googleFormatVersion = extra["googleJavaFormat.version"]
val autostyleVersion = extra["com.github.autostyle.version"]

dependencies {
    implementation("com.google.googlejavaformat:google-java-format:$googleFormatVersion")
    implementation("com.github.autostyle:com.github.autostyle.gradle.plugin:$autostyleVersion")
}

repositories {
    mavenCentral()
    gradlePluginPortal()
}
