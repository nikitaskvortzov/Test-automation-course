plugins {
    java
    id("io.qameta.allure")
}

dependencies {
    implementation("org.assertj:assertj-core:3.27.7")
    implementation("io.rest-assured:rest-assured:5.5.6")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.17.3")


    // @Step
    implementation("io.qameta.allure:allure-java-commons:2.35.4")

    // Allure-интеграция с REST Assured
    implementation("io.qameta.allure:allure-rest-assured:2.35.4")

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.junit.jupiter:junit-jupiter-engine")
    testImplementation("org.junit.jupiter:junit-jupiter-params")
}

allure {
    version.set("2.42.1")
}

tasks.test {
    useJUnitPlatform()
}

tasks.register<Test>("unitTests") {
    group = "verification"
    description = "Запускает unit-тесты"

    useJUnitPlatform {
        includeTags("unit_tests")
    }
}
