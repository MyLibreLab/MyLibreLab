pluginManagement {
    fun String.v() = extra["$this.version"].toString()
    plugins {
        fun PluginDependenciesSpec.idv(id: String, key: String = id) = id(id) version key.v()

        kotlin("jvm") version "kotlin".v()
        kotlin("kapt") version "kotlin".v()
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
    "application",
    "annotations",
    "dependencies-bom",
    "settings-api",
    "service-manager",
    "util"
)

for (p in rootProject.children) {
    if (p.children.isEmpty()) {
        // Rename leaf projects only
        // E.g. we don't expect to publish examples as a Maven module
        p.name = rootProject.name.toLowerCase() + "-" + p.name
    }
}

fun property(name: String) =
    when (extra.has(name)) {
        true -> extra.get(name) as? String
        else -> null
    }

property("localDarklaf")?.ifBlank { "../darklaf" }?.let {
    println("Importing project '$it'")
    includeBuild(it)
}
