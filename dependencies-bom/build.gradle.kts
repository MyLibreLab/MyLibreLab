plugins {
    `java-platform`
}

val String.v: String get() = rootProject.extra["$this.version"] as String

// Note: Gradle allows to declare dependency on "bom" as "api",
// and it makes the constraints to be transitively visible
// However Maven can't express that, so the approach is to use Gradle resolution
// and generate pom files with resolved versions
// See https://github.com/gradle/gradle/issues/9866

fun DependencyConstraintHandlerScope.apiv(
    notation: String,
    versionProp: String = notation.substringAfterLast(':')
) =
    "api"("$notation:${versionProp.v}")

fun DependencyConstraintHandlerScope.runtimev(
    notation: String,
    versionProp: String = notation.substringAfterLast(':')
) =
    "runtimeOnly"("$notation:${versionProp.v}")

dependencies {
    // Parenthesis are needed here: https://github.com/gradle/gradle/issues/9248
    (constraints) {
        // api means "the dependency is for both compilation and runtime"
        // runtime means "the dependency is only for runtime, not for compilation"
        // In other words, marking dependency as "runtime" would avoid accidental
        // dependency on it during compilation
        apiv("org.jetbrains:annotations", "jetbrains.annotations")
        apiv("com.google.guava:guava")
        apiv("javax.vecmath:vecmath")
        apiv("eu.hansolo:SteelSeries")
        apiv("org.pushing-pixels:trident")
        apiv("org.json:json")
        apiv("net.java.dev.jna:jna-platform", "jna")
        apiv("org.scream3r:jssc")
        apiv("org.bidib.jbidib:bidib-rxtx-binaries", "bidib-rxtx")

        apiv("org.netbeans.external:AbsoluteLayout")
        apiv("org.swinglabs:swing-layout")
        apiv("com.miglayout:miglayout-swing", "miglayout")
        apiv("com.github.weisj:darklaf-core", "darklaf")
        apiv("com.github.weisj:darklaf-property-loader", "darklaf")
        apiv("com.github.weisj:darklaf-extensions-kotlin", "darklaf-extensions")

        apiv("com.pi4j:pi4j-core", "pi4j")
        apiv("com.pi4j:pi4j-device", "pi4j")
        apiv("com.pi4j:pi4j-gpio-extension", "pi4j")
        apiv("com.pi4j:pi4j-service", "pi4j")

        apiv("javax.xml.bind:jaxb-api", "jaxb")
        apiv("com.sun.xml.bind:jaxb-core", "jaxb")
        apiv("com.sun.xml.bind:jaxb-impl", "jaxb")

        apiv("org.tinylog:tinylog-api", "tinylog")
        apiv("org.tinylog:tinylog-impl", "tinylog")

        apiv("com.google.code.findbugs:jsr305", "findbugs")
        apiv("org.junit.jupiter:junit-jupiter-api", "junit")
        apiv("org.junit.jupiter:junit-jupiter-engine", "junit")

        apiv("com.google.auto.service:auto-service-annotations", "auto-service")
        apiv("com.google.auto.service:auto-service", "auto-service")
        apiv("com.squareup:javapoet")
    }
}
