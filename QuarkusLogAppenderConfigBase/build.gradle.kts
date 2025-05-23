plugins {
    id("java")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(11))
    }
}


val quarkusVersion: String by project

dependencies {
    compileOnly("io.quarkus:quarkus-core:$quarkusVersion")

    implementation(project(":LogAppenderBase"))
}


ext["customArtifactId"] = "quarkus-log-appender-config-base"

apply(from = "../gradle/scripts/publish-codinux.gradle.kts")