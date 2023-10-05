plugins {
    kotlin("jvm")
}

java {
    withSourcesJar()

    toolchain {
        languageVersion.set(JavaLanguageVersion.of(8))
    }
}


val junitVersion: String by project

dependencies {
    api(project(":LogAppenderBase"))

    api("ch.qos.logback:logback-classic:1.3.0")


    testImplementation(platform("org.junit:junit-bom:$junitVersion"))
    testImplementation("org.junit.jupiter:junit-jupiter")

    testImplementation("io.mockk:mockk:1.13.5")
    testImplementation("org.assertj:assertj-core:3.24.2")
}

tasks.test {
    useJUnitPlatform()
}


ext["customArtifactId"] = "logback-appender-base"

apply(from = "../gradle/scripts/publish-codinux.gradle.kts")