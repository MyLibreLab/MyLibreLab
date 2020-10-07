pluginManagement {
    plugins {
        fun String.v() = extra["$this.version"].toString()
        fun PluginDependenciesSpec.idv(id: String, key: String = id) = id(id) version key.v()

        idv("com.github.autostyle")
        idv("org.sonarqube")
        idv("com.github.vlsi.crlf", "com.github.vlsi.vlsi-release-plugins")
        idv("com.github.vlsi.gradle-extensions", "com.github.vlsi.vlsi-release-plugins")
        idv("org.beryx.runtime")
        idv("name.remal.sonarlint")
    }
}

rootProject.name = "MyLibreLab"

include(
    "dependencies-bom"
)

for (p in rootProject.children) {
    if (p.children.isEmpty()) {
        // Rename leaf projects only
        // E.g. we don't expect to publish examples as a Maven module
        p.name = rootProject.name.toLowerCase() + "-" + p.name
    }
}
