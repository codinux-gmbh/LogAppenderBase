pluginManagement {
    val kotlinVersion: String by settings
    val quarkusVersion: String by settings

    repositories {
        mavenCentral()
        gradlePluginPortal()
    }

    plugins {
        kotlin("multiplatform") version kotlinVersion
        kotlin("jvm") version kotlinVersion
        kotlin("plugin.serialization") version kotlinVersion

        id("io.quarkus") version quarkusVersion
        id("io.quarkus.extension") version quarkusVersion

        id("me.champeau.jmh") version "0.7.1"
    }
}


rootProject.name = "LogAppenderBase"


include("LogAppenderBase")

include("LogbackAppenderBase")
include("Log4j2LogAppenderBase")
include("JBossLoggingAppenderBase")
include("JavaUtilLogAppenderBase")

include("QuarkusLogAppenderConfigBase")


/*      Kubernetes info retriever       */

include("Fabric8KubernetesInfoRetriever")
project(":Fabric8KubernetesInfoRetriever").apply {
    projectDir = File("kubernetes/Fabric8KubernetesInfoRetriever")
}

include("CodinuxKubernetesInfoRetriever")
project(":CodinuxKubernetesInfoRetriever").apply {
    projectDir = File("kubernetes/CodinuxKubernetesInfoRetriever")
}


// include to run benchmarks but breaks publishing libraries with error:
// The Kotlin Gradle plugin was loaded multiple times in different subprojects, which is not supported and may break the build.
//include("benchmarks")
