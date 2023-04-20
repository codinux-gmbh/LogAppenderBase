plugins {
    kotlin("jvm")
}

val junitVersion: String by project

dependencies {
    api("$group:log-appender-base:$version")
//    api(project(":log-appender-base"))

    api("ch.qos.logback:logback-classic:1.2.7")

    testImplementation(platform("org.junit:junit-bom:$junitVersion"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}