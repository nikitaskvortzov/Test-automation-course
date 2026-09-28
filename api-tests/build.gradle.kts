plugins {
    java
    id("io.qameta.allure")
}

dependencies {
    implementation(project(":common"))
    testImplementation("com.fasterxml.jackson.core:jackson-databind:2.17.3")

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.junit.jupiter:junit-jupiter-engine")
    testImplementation("org.junit.jupiter:junit-jupiter-params")

    testImplementation("org.assertj:assertj-core:3.27.7")
    testImplementation("io.rest-assured:rest-assured:5.5.6")

    testImplementation("io.qameta.allure:allure-junit5:2.35.4")
    testImplementation("io.qameta.allure:allure-rest-assured:2.35.4")
}

allure {
    version.set("2.42.1")
}

tasks.test {
    useJUnitPlatform()
}

tasks.register<Test>("smokeApiTest") {
    group = "verification"
    description = "Запускает API Smoke-тесты"

    dependsOn(tasks.testClasses)

    testClassesDirs = sourceSets["test"].output.classesDirs
    classpath = sourceSets["test"].runtimeClasspath

    useJUnitPlatform {
        includeTags("smoke")
    }

    include("**/Api_tests/**/*.class")
}
