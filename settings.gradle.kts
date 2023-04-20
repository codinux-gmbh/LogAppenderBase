pluginManagement {
    val kotlinVersion: String by settings

    repositories {
        mavenCentral()
        gradlePluginPortal()
    }

    plugins {
        kotlin("jvm") version kotlinVersion
        kotlin("multiplatform") version kotlinVersion
    }
}


rootProject.name = "LogAppenderBase"


include("LogAppenderBase")
project(":LogAppenderBase").name = "log-appender-base"

include("JBossLoggingAppender")
project(":JBossLoggingAppender").name = "jboss-logging-appender"
