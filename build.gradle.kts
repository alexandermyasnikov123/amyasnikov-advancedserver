import org.springframework.boot.gradle.plugin.SpringBootPlugin

plugins {
    java
    application
    `kotlin-dsl`
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.dependency.management)
}

group = "net.dunice"
version = "0.0.1-SNAPSHOT"

application {
    mainClass = AppConstants.MAIN_CLASS
}

subprojects {
    plugins.withType<SpringBootPlugin> {
        tasks.bootJar {
            enabled = false
        }
    }
}

allprojects {
    repositories {
        mavenCentral()
    }


    plugins.withType<JavaPlugin> {
        java {
            toolchain {
                languageVersion = AppConstants.javaVersion
            }
        }
        dependencies {
            compileOnly(libs.lombok)
            annotationProcessor(libs.lombok)

            testCompileOnly(libs.lombok)
            testAnnotationProcessor(libs.lombok)
        }
    }

    plugins.withType<SpringBootPlugin> {
        dependencies {
            implementation(libs.spring.log4j)
            modules {
                module(libs.spring.logback.get().module) {
                    replacedBy(libs.spring.log4j.get().module)
                }
            }
            configurations.all {
                exclude(group = AppConstants.EXCLUDE_LOGBACK_GROUP, module = AppConstants.EXCLUDE_LOGBACK_MODULE)
            }
        }
    }
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

dependencies {
    developmentOnly(libs.spring.dev.tools)

    implementation(libs.spring.jpa)
    implementation(libs.spring.web)
    implementation(libs.spring.validation)
    runtimeOnly(libs.bundles.database.commons)

    implementation(libs.spring.eureka.server)
    implementation(libs.spring.actuator)

    implementation(libs.liquibase)

    testImplementation(libs.spring.starter.test)
    testImplementation(libs.spring.security.test)
    testRuntimeOnly(libs.junit.platform.launcher)

    implementation(project(":features:core"))
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