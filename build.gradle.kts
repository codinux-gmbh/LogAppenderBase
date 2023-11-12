plugins {
    // don't know why it's needed but otherwise build breaks with "The Kotlin Gradle plugin was loaded multiple times in different subprojects"
    kotlin("plugin.serialization") apply(false)
}


allprojects {
    repositories {
        mavenCentral()
        google()
        mavenLocal()
    }

    group = "net.codinux.log"
    version = "0.5.3-SNAPSHOT"


    ext["sourceCodeRepositoryBaseUrl"] = "github.com/codinux/LogAppenderBase"

    ext["projectDescription"] = "Common log appender implementation for log appenders like ElasticSearchLogAppender and LokiLogAppender"
}

tasks.register("publishAllToMavenLocal") {
    dependsOn(
        ":LogAppenderBase:publishToMavenLocal",

        ":LogbackAppenderBase:publishToMavenLocal",
        ":JBossLoggingAppenderBase:publishToMavenLocal",
        ":JavaUtilLogAppenderBase:publishToMavenLocal",

        ":QuarkusLogAppenderConfigBase:publishToMavenLocal",

        ":CodinuxKubernetesInfoRetriever:publishToMavenLocal",
        ":Fabric8KubernetesInfoRetriever:publishToMavenLocal"
    )
}

tasks.register("publishAll") {
    dependsOn(
        ":LogAppenderBase:publish",

        ":LogbackAppenderBase:publish",
        ":JBossLoggingAppenderBase:publish",
        ":JavaUtilLogAppenderBase:publish",

        ":QuarkusLogAppenderConfigBase:publish",

        ":CodinuxKubernetesInfoRetriever:publish",
        ":Fabric8KubernetesInfoRetriever:publish"
    )
}
