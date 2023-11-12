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
val mockkVersion: String by project
val assertJVersion: String by project

dependencies {
    api(project(":LogAppenderBase"))

    api("ch.qos.logback:logback-classic:1.3.0")


    testImplementation(platform("org.junit:junit-bom:$junitVersion"))
    testImplementation("org.junit.jupiter:junit-jupiter")

    testImplementation("io.mockk:mockk:$mockkVersion")
    testImplementation("org.assertj:assertj-core:$assertJVersion")
}

tasks.test {
    useJUnitPlatform()
}


ext["customArtifactId"] = "logback-appender-base"

apply(from = "../gradle/scripts/publish-codinux.gradle.kts")