plugins {
    id("java")
    id("io.qameta.allure") version "4.1.0"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    // JUnit 5
    implementation(platform("org.junit:junit-bom:5.10.0"))
    implementation("org.junit.jupiter:junit-jupiter")
    implementation("org.junit.jupiter:junit-jupiter-engine")
    implementation("org.junit.jupiter:junit-jupiter-params")

    // AssertJ
    implementation("org.assertj:assertj-core:3.27.7")

    // REST Assured
    implementation("io.rest-assured:rest-assured:5.5.6")

    // Selenium
    implementation("org.seleniumhq.selenium:selenium-java:4.47.0")
    implementation("io.github.bonigarcia:webdrivermanager:5.4.0")

    // Selenide
    implementation("com.codeborne:selenide:7.17.0")

    // Логирование
    implementation("org.slf4j:slf4j-simple:2.0.7")

    // Allure
    implementation("io.qameta.allure:allure-jupiter:2.35.4")
    implementation("io.qameta.allure:allure-selenide:2.35.4")
    implementation("io.qameta.allure:allure-rest-assured:2.35.4")
}

allure {
    version.set("2.42.1")
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
