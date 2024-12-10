plugins {
    `java-library`
    `kotlin-dsl`
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.dependency.management)
}

group = "net.dunice.features.auth"
version = "0.0.1-SNAPSHOT"

dependencies {
    developmentOnly(libs.spring.dev.tools)

    implementation(libs.bundles.spring.commons)

    runtimeOnly(libs.h2.db)

    implementation(libs.bundles.feign.commons)

    implementation(libs.jwt.api)
    runtimeOnly(libs.jwt.impl)
    runtimeOnly(libs.jwt.jackson)

    api(project(":features:core"))
}

tasks.test {
    useJUnitPlatform()
}