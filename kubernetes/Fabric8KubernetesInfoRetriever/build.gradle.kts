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
    implementation(project(":LogAppenderBase"))

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
ext["customArtifactId"] = "fabric8-kubernetes-info-retriever"

apply(from = "../../gradle/scripts/publish-codinux.gradle.kts")