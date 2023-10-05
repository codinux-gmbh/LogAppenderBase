pluginManagement {
    val kotlinVersion: String by settings
    val quarkusVersion: String by settings

    repositories {
        mavenCentral()
        gradlePluginPortal()
    }

    plugins {
        kotlin("multiplatform") version kotlinVersion apply(false)
        kotlin("jvm") version kotlinVersion apply(false)
        kotlin("plugin.serialization") version kotlinVersion apply(false)

        id("io.quarkus") version quarkusVersion
        id("io.quarkus.extension") version quarkusVersion
    }
}


rootProject.name = "LogAppenderBase"


include("LogAppenderBase")

include("LogbackAppenderBase")
include("JBossLoggingAppenderBase")
include("JavaUtilLogAppenderBase")

include("QuarksLogAppenderConfigBase")


/*      Kubernetes info retriever       */

include("Fabric8KubernetesInfoRetriever")
project(":Fabric8KubernetesInfoRetriever").apply {
    projectDir = File("kubernetes/Fabric8KubernetesInfoRetriever")
}

include("CodinuxKubernetesInfoRetriever")
project(":CodinuxKubernetesInfoRetriever").apply {
    projectDir = File("kubernetes/CodinuxKubernetesInfoRetriever")
}
