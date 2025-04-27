plugins {
    `java-library`
}

repositories {
    mavenCentral()
}

dependencies {
    api("jakarta.annotation:jakarta.annotation-api:3.0.0")
    testImplementation("org.assertj:assertj-core:3.27.3")
}

testing {
    suites {
        val test by getting(JvmTestSuite::class) {
            useJUnitJupiter("5.11.3")
        }
    }
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}
