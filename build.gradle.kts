/*
 * This file was generated by the Gradle 'init' task.
 *
 * This generated file contains a sample Java application project to get you started.
 * For more details on building Java & JVM projects, please refer to https://docs.gradle.org/8.11/userguide/building_java_projects.html in the Gradle documentation.
 */

plugins {
    // Apply the application plugin to add support for building a CLI application in Java.
    application
}

repositories {
    // Use Maven Central for resolving dependencies.
    mavenCentral()
}

dependencies {
    // Use JUnit Jupiter for testing.
//    testImplementation(libs.junit.jupiter)
//    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    testImplementation("org.junit.jupiter:junit-jupiter:5.11.3")
    testImplementation("org.mockito:mockito-core:4.2.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.11.3")

    // This dependency is used by the application.
    implementation(libs.guava)
}


// Apply a specific Java toolchain to ease working on different environments.
java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

application {
    // Define the main class for the application.
    mainClass = "engine.Core"
}

sourceSets {
    main {
        java {
            setSrcDirs(listOf("src")) // 모든 실행 소스를 포함
            exclude("**/Test/**")    // 테스트 디렉토리는 제외
        }
        resources {
            setSrcDirs(listOf("res")) // 리소스 디렉토리 설정
        }
    }
    test {
        java {
            setSrcDirs(listOf("src/Test")) // 테스트 코드 경로 설정
        }
    }
}

tasks.named<Test>("test") {
    // Use JUnit Platform for unit tests.
    useJUnitPlatform()
}
