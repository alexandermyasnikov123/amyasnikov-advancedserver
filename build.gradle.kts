plugins {
    java
    application
    id("org.springframework.boot") version "3.3.5"
    id("io.spring.dependency-management") version "1.1.6"
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

repositories {
    mavenCentral()
}

dependencies {
    compileOnly("org.projectlombok:lombok")
    testCompileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")

    developmentOnly("org.springframework.boot:spring-boot-devtools")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-data-jdbc")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")

    implementation("com.h2database:h2")
    runtimeOnly("org.postgresql:postgresql")

    implementation("org.liquibase:liquibase-core")
    implementation("net.coobird:thumbnailator:0.4.20")

    implementation(libs.map.struct)
    annotationProcessor(libs.map.struct.apt)

    implementation(libs.jwt.api)
    runtimeOnly(libs.jwt.impl)
    runtimeOnly(libs.jwt.jackson)

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.security:spring-security-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

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