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
    implementation(platform("io.quarkus:quarkus-bom:${quarkusVersion}"))
    implementation("io.quarkus:quarkus-core")

    implementation("$group:log-appender-base:$version")
//    implementation(project(":log-appender-base"))
}


ext["artifactId"] = project.name
ext["libraryName"] = ext["artifactId"]


val commonScriptsFile = File(File(project.gradle.gradleUserHomeDir, "scripts"), "commonScripts.gradle")
if (commonScriptsFile.exists()) {
    apply(from = commonScriptsFile)
}