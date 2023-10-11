plugins {
    kotlin("jvm")
}

java {
    withSourcesJar()

    toolchain {
        // ExtLogRecord.instant needs at least Java version 9
        languageVersion.set(JavaLanguageVersion.of(9))
    }
}


val junitVersion: String by project

dependencies {
    api(project(":LogAppenderBase"))

    api("org.jboss.logmanager:jboss-logmanager-embedded:1.0.9")

    testImplementation(platform("org.junit:junit-bom:$junitVersion"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}


ext["customArtifactId"] = "jboss-logging-appender-base"

apply(from = "../gradle/scripts/publish-codinux.gradle.kts")