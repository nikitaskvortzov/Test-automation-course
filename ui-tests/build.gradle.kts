plugins {
    java
    id("io.qameta.allure")
}

dependencies {
    implementation(project(":common"))

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.junit.jupiter:junit-jupiter-engine")
    testImplementation("org.junit.jupiter:junit-jupiter-params")

    testImplementation("org.assertj:assertj-core:3.27.7")

    testImplementation("org.seleniumhq.selenium:selenium-java:4.47.0")
    testImplementation("io.github.bonigarcia:webdrivermanager:5.4.0")
    testImplementation("com.codeborne:selenide:7.17.0")

    testRuntimeOnly("org.slf4j:slf4j-simple:2.0.7")

    testImplementation("io.qameta.allure:allure-junit5:2.35.4")
    testImplementation("io.qameta.allure:allure-selenide:2.35.4")
}

allure {
    version.set("2.42.1")
}

tasks.test {
    useJUnitPlatform()
}

tasks.register<Test>("smokeUiTest") {
    group = "verification"
    description = "Запускает UI Smoke-тесты"

    dependsOn(tasks.testClasses)

    testClassesDirs = sourceSets["test"].output.classesDirs
    classpath = sourceSets["test"].runtimeClasspath

    useJUnitPlatform {
        includeTags("smoke")
    }

    include("**/Ui_tests/**/*.class")
}
