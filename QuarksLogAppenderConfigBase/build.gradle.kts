plugins {
    id("java")
}

java {
    withSourcesJar()

    toolchain {
        languageVersion.set(JavaLanguageVersion.of(11))
    }
}


val quarkusVersion: String by project

dependencies {
    compileOnly(platform("io.quarkus:quarkus-bom:${quarkusVersion}"))
    compileOnly("io.quarkus:quarkus-core")

    implementation(project(":LogAppenderBase"))
}


ext["customArtifactId"] = "quarkus-log-appender-config-base"

apply(from = "../gradle/scripts/publish-codinux.gradle.kts")