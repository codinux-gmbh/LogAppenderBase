plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(11))
    }
}


val coroutinesVersion: String by project
val kotlinSerializationVersion: String by project
val junitVersion: String by project

dependencies {
    implementation(project(":LogAppenderBase"))

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$kotlinSerializationVersion")


    testImplementation(platform("org.junit:junit-bom:$junitVersion"))
    testImplementation("org.junit.jupiter:junit-jupiter")

    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:$coroutinesVersion")
}

tasks.test {
    useJUnitPlatform()
}


group = "$group.kubernetes"
ext["customArtifactId"] = "codinux-kubernetes-info-retriever"

apply(from = "../../gradle/scripts/publish-codinux.gradle.kts")