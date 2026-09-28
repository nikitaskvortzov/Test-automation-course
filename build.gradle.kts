plugins {
    id("io.qameta.allure") version "4.1.0" apply false
}

allprojects {
    group = "org.example"
    version = "1.0-SNAPSHOT"

    repositories {
        mavenCentral()
    }
}

subprojects {
    tasks.withType<Test>().configureEach {
        useJUnitPlatform()

        testLogging {
            events(
                "PASSED",
                "FAILED",
                "SKIPPED"
            )
        }
    }
}
