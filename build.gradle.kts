plugins {
    java
    application
    `kotlin-dsl`
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.dependency.management)
}

group = "net.dunice"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

application {
    mainClass = "net.dunice.newsapi.NewsApiApplication"
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

dependencies {
    compileOnly(libs.lombok)
    testCompileOnly(libs.lombok)
    annotationProcessor(libs.lombok)

    developmentOnly(libs.spring.dev.tools)

    implementation(libs.bundles.spring.commons)

    runtimeOnly(libs.bundles.database.commons)

    implementation(libs.liquibase)
    implementation(libs.thumbnailator)

    implementation(libs.map.struct)
    annotationProcessor(libs.map.struct.apt)

    implementation(libs.jwt.api)
    runtimeOnly(libs.jwt.impl)
    runtimeOnly(libs.jwt.jackson)

    testImplementation(libs.spring.starter.test)
    testImplementation(libs.spring.security.test)
    testRuntimeOnly(libs.junit.platform.launcher)

}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.register("bootRunDev") {
    group = "application"
    description = "Runs the application with 'dev' profile"

    dependsOn(tasks.bootRun.get().apply {
        environment("SPRING_PROFILES_ACTIVE" to "dev")
    })
}

tasks.register("bootRunProd") {
    group = "application"
    description = "Runs the application with 'prod' profile"

    dependsOn(tasks.bootRun.get().apply {
        environment("SPRING_PROFILES_ACTIVE" to "prod")
    })
}