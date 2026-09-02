
// build.gradle.kts (Kotlin DSL)
import java.util.concurrent.atomic.AtomicBoolean

plugins {
    id("java")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
    testImplementation("org.junit.jupiter:junit-jupiter-params")
    testImplementation("org.assertj:assertj-core:3.27.7")
    implementation("io.rest-assured:rest-assured:5.5.6")
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.0")
    implementation("org.seleniumhq.selenium:selenium-java:4.47.0")
    implementation("io.github.bonigarcia:webdrivermanager:5.4.0")
    implementation("org.slf4j:slf4j-simple:2.0.7")
    implementation("com.codeborne:selenide:7.17.0")
}

tasks.named<Test>("test") {
    useJUnitPlatform()

    testLogging {
        events("PASSED", "FAILED", "SKIPPED")
    }

    finalizedBy("finalizeTestRun")
}

tasks.register("runAllTests") {
    group = "verification"
    description = "Запускает все тесты проекта."
    dependsOn(tasks.named("test"))
}

tasks.register("finalizeTestRun") {
    doLast {
        println("Test run is over")
    }
}