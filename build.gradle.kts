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
    // Source: https://mvnrepository.com/artifact/org.assertj/assertj-core
    testImplementation("org.assertj:assertj-core:3.27.7")
    // Source: https://mvnrepository.com/artifact/io.rest-assured/rest-assured
    implementation("io.rest-assured:rest-assured:5.5.6")
}

tasks.test {
    useJUnitPlatform()
}

val runAllTests by tasks.registering(Test::class) {
    description = "Запускает все тесты проекта."
    group = "verification"
    useJUnitPlatform()

    testLogging {
        events("PASSED", "FAILED", "SKIPPED")
    }
}

val firstTestDone = AtomicBoolean(false)

tasks.register("finalizeTestRun") {
    dependsOn(runAllTests)
    doLast {
        println("Test run is over")
    }
}

tasks.named("test") {
    finalizedBy("finalizeTestRun")
}

