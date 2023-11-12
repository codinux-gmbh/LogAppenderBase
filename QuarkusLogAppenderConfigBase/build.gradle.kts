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
    compileOnly(platform("io.quarkus:quarkus-bom:$quarkusVersion"))
    compileOnly("io.quarkus:quarkus-core")

    // TODO: remove again as soon as Target_net_codinux_log_LoggerFactory is moved to an extra project
    compileOnly("io.quarkus.arc:arc:$quarkusVersion")
    compileOnly("net.codinux.log:kmp-log:1.1.2")
    compileOnly("org.graalvm.sdk:graal-sdk:23.0.1")

    implementation(project(":LogAppenderBase"))
}


ext["customArtifactId"] = "quarkus-log-appender-config-base"

apply(from = "../gradle/scripts/publish-codinux.gradle.kts")