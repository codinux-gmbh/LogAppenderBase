plugins {
    kotlin("jvm")
}

java {
    toolchain {
        // ExtLogRecord.instant needs at least Java version 9
        languageVersion.set(JavaLanguageVersion.of(11))
    }
}


val junitVersion: String by project
val mockkVersion: String by project
val assertJVersion: String by project

dependencies {
    api(project(":LogAppenderBase"))

    // 3.0.0.Final works with Quarkus 3.30+, but cannot say if it also works with older Quarkus versions
    api("org.jboss.logmanager:jboss-logmanager:3.0.0.Final")

    testImplementation(platform("org.junit:junit-bom:$junitVersion"))
    testImplementation("org.junit.jupiter:junit-jupiter")

    testImplementation("io.mockk:mockk:$mockkVersion")
    testImplementation("org.assertj:assertj-core:$assertJVersion")

    testImplementation("org.jboss.slf4j:slf4j-jboss-logging:1.2.1.Final")
}

tasks.test {
    useJUnitPlatform()
}


ext["customArtifactId"] = "jboss-logging-appender-base"

apply(from = "../gradle/scripts/publish-codinux.gradle.kts")