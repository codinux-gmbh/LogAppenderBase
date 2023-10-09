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
    implementation(platform("io.quarkus:quarkus-bom:${quarkusVersion}"))
    implementation("io.quarkus:quarkus-core")

    implementation("$group:log-appender-base:$version")
//    implementation(project(":log-appender-base"))
}


ext["customArtifactId"] = "quarkus-log-appender-config-base"

apply(from = "../gradle/scripts/publish-codinux.gradle.kts")