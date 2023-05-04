plugins {
    kotlin("jvm")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(8))
    }
}


val junitVersion: String by project

dependencies {
    implementation("$group:log-appender-base:$version")
//    implementation(project(":log-appender-base"))

    implementation("io.fabric8:kubernetes-client:6.5.1")
    // TODO: or use JDK HttpClient? Wouldn't conflict with other OkHttp usages
    implementation("io.fabric8:kubernetes-httpclient-okhttp:6.5.1")

    testImplementation(platform("org.junit:junit-bom:$junitVersion"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}


group = "$group.kubernetes"
ext["artifactName"] = "fabric8-kubernetes-info-retriever"


val commonScriptsFile = File(File(project.gradle.gradleUserHomeDir, "scripts"), "commonScripts.gradle")
if (commonScriptsFile.exists()) {
    apply(from = commonScriptsFile)
}