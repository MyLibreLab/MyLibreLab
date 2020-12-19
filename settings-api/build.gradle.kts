plugins {
    kotlin("jvm")
    kotlin("kapt")
}

dependencies {
    compileOnly(project(":mylibrelab-annotations"))
    implementation(project(":mylibrelab-service-manager"))
    implementation(project(":mylibrelab-util"))
    implementation("org.tinylog:tinylog-api")
}
