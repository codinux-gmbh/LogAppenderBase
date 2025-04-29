// don't know why but we have to add jvm plugin this way otherwise compilation fails
plugins {
//    val kotlinVersion: String by settings
    val kotlinVersion = "1.9.23"

    kotlin("jvm") version kotlinVersion apply(false)
}


allprojects {
    repositories {
        mavenCentral()
        google()
    }

    group = "net.codinux.log"
    version = "0.6.2-SNAPSHOT"


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
