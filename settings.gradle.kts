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

include("LogbackAppenderBase")
project(":LogbackAppenderBase").name = "logback-appender-base"

include("JBossLoggingAppenderBase")
project(":JBossLoggingAppenderBase").name = "jboss-logging-appender-base"

include("JavaUtilLogAppenderBase")
project(":JavaUtilLogAppenderBase").name = "java-util-log-appender-base"
