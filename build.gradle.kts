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
    version = "1.0.0-SNAPSHOT"


    ext["sourceCodeRepositoryBaseUrl"] = "github.com/codinux/LogAppenderBase"

    ext["projectDescription"] = "Common log appender implementation for log appenders like ElasticSearchLogAppender and LokiLogAppender"
}

tasks.register("publishAllToMavenLocal") {
    dependsOn(
        ":LogAppenderBase:publishToMavenLocal",

        ":LogbackAppenderBase:publishToMavenLocal",
        ":JBossLoggingAppenderBase:publishToMavenLocal",
        ":JavaUtilLogAppenderBase:publishToMavenLocal",

        ":QuarksLogAppenderConfigBase:publishToMavenLocal",

        ":CodinuxKubernetesInfoRetriever:publishToMavenLocal",
        ":Fabric8KubernetesInfoRetriever:publishToMavenLocal"
    )
}
