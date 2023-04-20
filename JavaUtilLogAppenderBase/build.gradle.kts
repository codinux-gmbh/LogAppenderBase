plugins {
    kotlin("jvm")
}


val junitVersion: String by project

dependencies {
    api("$group:log-appender-base:$version")
//    api(project(":log-appender-base"))

    testImplementation(platform("org.junit:junit-bom:$junitVersion"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}


ext["artifactId"] = project.name
ext["libraryName"] = ext["artifactId"]


val commonScriptsFile = File(File(project.gradle.gradleUserHomeDir, "scripts"), "commonScripts.gradle")
if (commonScriptsFile.exists()) {
    apply(from = commonScriptsFile)
}