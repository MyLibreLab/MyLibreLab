plugins {
    `java-library`
}

dependencies {
    api(project(":mylibrelab-annotations"))
    implementation("org.tinylog:tinylog-api")
    implementation("org.jetbrains:annotations")
}
