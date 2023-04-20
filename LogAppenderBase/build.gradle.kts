plugins {
    kotlin("multiplatform")
}

group = "net.codinux.log"
version = "1.0.0-SNAPSHOT"


kotlin {
    jvm {
        jvmToolchain(8)
        withJava()
        testRuns["test"].executionTask.configure {
            useJUnitPlatform()
        }
    }

    js(IR) {
        binaries.executable()

        browser {
            commonWebpackConfig {
                cssSupport {
                    enabled.set(true)
                }
            }
            testTask {
                useKarma {
                    useChromeHeadless()
                    useFirefoxHeadless()
                }
            }
        }

        nodejs {

        }
    }

    val hostOs = System.getProperty("os.name")
    val isMingwX64 = hostOs.startsWith("Windows")
    val nativeTarget = when {
        hostOs == "Mac OS X" -> macosX64("native")
        hostOs == "Linux" -> linuxX64("native")
        isMingwX64 -> mingwX64("native")
        else -> throw GradleException("Host OS is not supported in Kotlin/Native.")
    }


    sourceSets {
        val commonMain by getting {
            dependencies {
                api("org.jetbrains.kotlinx:kotlinx-datetime:0.4.0")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }

        val jvmMain by getting
        val jvmTest by getting

        val jsMain by getting
        val jsTest by getting

        val nativeMain by getting
        val nativeTest by getting
    }
}


ext["groupId"] = group
ext["artifactId"] = "log-appender-base" // is overwritten by Gradle anyway, therefore we need to set in in settings.gradle
ext["artifactVersion"] = version
ext["libraryName"] = ext["artifactId"]

ext["sourceCodeRepositoryBaseUrl"] = "https://github.com/codinux/LogAppenderBase"

ext["useNewSonatypeRepo"] = true
ext["packageGroup"] = "net.codinux"

ext["projectDescription"] = "Common implementation for loggers like ElasticSearchLogger and LokiLogger"

ext["developerId"] = "codinux"
ext["developerName"] = "codinux GmbH & Co. KG"
ext["developerMail"] = "git@codinux.net"

ext["licenseName"] = "The Apache License, Version 2.0"
ext["licenseUrl"] = "http://www.apache.org/licenses/LICENSE-2.0.txt"

apply(from = "../publish.gradle.kts")
