plugins {
    `java-library`
    kotlin("jvm")
    kotlin("kapt")
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}
