pluginManagement {
    val kotlinVersion: String by settings
    val quarkusVersion: String by settings

    repositories {
        mavenCentral()
        gradlePluginPortal()
    }

    plugins {
        kotlin("jvm") version kotlinVersion
        kotlin("multiplatform") version kotlinVersion

        id("io.quarkus") version quarkusVersion
        id("io.quarkus.extension") version quarkusVersion
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

include("QuarksLogAppenderConfigBase")
project(":QuarksLogAppenderConfigBase").name = "quarks-log-appender-config-base"
