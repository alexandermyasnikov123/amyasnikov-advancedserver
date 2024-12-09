plugins {
    `java-library`
    `kotlin-dsl`
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.dependency.management)
}

group = "net.dunice.features.auth"
version = "0.0.1-SNAPSHOT"

dependencies {
    compileOnly(libs.lombok)
    testCompileOnly(libs.lombok)
    annotationProcessor(libs.lombok)

    developmentOnly(libs.spring.dev.tools)

    implementation(libs.bundles.spring.commons)

    runtimeOnly(libs.bundles.database.commons)

    implementation(libs.jwt.api)
    runtimeOnly(libs.jwt.impl)
    runtimeOnly(libs.jwt.jackson)

    implementation(libs.map.struct)
    annotationProcessor(libs.map.struct.apt)

    api(project(":features:core"))
    api(project(":features:shared"))
    api(project(":features:users"))
}

tasks.test {
    useJUnitPlatform()
}