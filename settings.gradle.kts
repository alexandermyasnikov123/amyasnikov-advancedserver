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
include("features:news")
findProject(":features:news")?.name = "news"
include("features:user-shared-dtos")
findProject(":features:user-shared-dtos")?.name = "user-shared-dtos"
