plugins {
    kotlin("jvm")
}

java {
    withSourcesJar()
}


val junitVersion: String by project

dependencies {
    api(project(":LogAppenderBase"))

    testImplementation(platform("org.junit:junit-bom:$junitVersion"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}


ext["customArtifactId"] = "java-util-log-appender-base"

apply(from = "../gradle/scripts/publish-codinux.gradle.kts")