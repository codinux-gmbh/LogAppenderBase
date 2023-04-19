pluginManagement {
    val kotlinVersion: String by settings

    repositories {
        mavenCentral()
        gradlePluginPortal()
    }

    plugins {
        kotlin("multiplatform") version kotlinVersion
    }
}


rootProject.name = "LoggerBase"


include("LoggerBase")
project(":LoggerBase").name = "logger-base"

