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
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")

                implementation("org.jetbrains.kotlinx:atomicfu:0.20.2")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))

                implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.4.0")

                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.6.4")

                implementation("io.kotest:kotest-assertions-core:5.6.1")
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


ext["artifactId"] = project.name
ext["libraryName"] = ext["artifactId"]


apply(from = "../publish.gradle.kts")
