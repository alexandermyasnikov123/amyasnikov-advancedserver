dependencyResolutionManagement {
    repositories {
        mavenCentral()
        google()
    }
}

fileTree("features").matching {
    include("*/build.gradle.kts")
}.forEach { file ->
    val moduleName = file.parentFile.name
    include(":features:$moduleName")
    project(":features:$moduleName").projectDir = file.parentFile
}
include("features:tags")
findProject(":features:tags")?.name = "tags"
