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

// Общая настройка тестов
tasks.withType<Test>().configureEach {
    useJUnitPlatform()

    testLogging {
        events(
            "PASSED",
            "FAILED",
            "SKIPPED"
        )
    }

    finalizedBy("finalizeTestRun")
}

// Запуск всех тестов
tasks.named<Test>("test") {
    include("**/*.class")
}

// API Smoke-тесты
tasks.register<Test>("smokeApiTest") {
    group = "verification"
    description = "Запускает API Smoke-тесты"

    dependsOn(tasks.named("testClasses"))

    useJUnitPlatform {
        includeTags("smoke")
    }

    include(
        "**/Api_tests/**/*Test.class",
        "**/Api_tests/**/*Tests.class",
        "**/Api_tests/**/*TestCase.class"
    )

    testClassesDirs = sourceSets["test"].output.classesDirs
    classpath = sourceSets["test"].runtimeClasspath
}

// UI Smoke-тесты
tasks.register<Test>("smokeUiTest") {
    group = "verification"
    description = "Запускает UI Smoke-тесты"

    dependsOn(tasks.named("testClasses"))

    useJUnitPlatform {
        includeTags("smoke")
    }

    include(
        "**/UI_tests/**/*Test.class",
        "**/UI_tests/**/*Tests.class",
        "**/UI_tests/**/*TestCase.class"
    )

    testClassesDirs = sourceSets["test"].output.classesDirs
    classpath = sourceSets["test"].runtimeClasspath
}

// Общий Smoke-прогон API и UI
tasks.register("smokeTest") {
    group = "verification"
    description = "Запускает API и UI Smoke-тесты"

    dependsOn(
        tasks.named("smokeApiTest"),
        tasks.named("smokeUiTest")
    )
}

// Запуск всех тестов проекта
tasks.register("runAllTests") {
    group = "verification"
    description = "Запускает все тесты проекта"

    dependsOn(tasks.named("test"))
}

// Завершение прогона
tasks.register("finalizeTestRun") {
    doLast {
        println("Test run is over")
    }
}

