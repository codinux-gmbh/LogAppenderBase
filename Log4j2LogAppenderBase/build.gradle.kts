plugins {
    kotlin("jvm")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(9))
    }
}


val log4j2Version = "2.20.0"
val junitVersion: String by project

dependencies {
    api(project(":LogAppenderBase"))

    api("org.apache.logging.log4j:log4j-core:$log4j2Version")

    testImplementation(platform("org.junit:junit-bom:$junitVersion"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}


ext["customArtifactId"] = "log4j2-log-appender-base"

apply(from = "../gradle/scripts/publish-codinux.gradle.kts")