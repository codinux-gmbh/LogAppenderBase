
allprojects {
    repositories {
        mavenCentral()
        google()
        mavenLocal()
    }

    group = "net.codinux.log"
    version = "1.0.0-SNAPSHOT"


    ext["groupId"] = group
    ext["artifactVersion"] = version

    ext["sourceCodeRepositoryBaseUrl"] = "https://github.com/codinux/LogAppenderBase"

    ext["useNewSonatypeRepo"] = true
    ext["packageGroup"] = "net.codinux"

    ext["projectDescription"] = "Common log appender implementation for log appenders like ElasticSearchLogAppender and LokiLogAppender"

    ext["developerId"] = "codinux"
    ext["developerName"] = "codinux GmbH & Co. KG"
    ext["developerMail"] = "git@codinux.net"

    ext["licenseName"] = "The Apache License, Version 2.0"
    ext["licenseUrl"] = "http://www.apache.org/licenses/LICENSE-2.0.txt"
}
