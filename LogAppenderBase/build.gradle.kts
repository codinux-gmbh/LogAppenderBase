plugins {
    kotlin("multiplatform")
}


kotlin {
    // Enable the default target hierarchy:
    targetHierarchy.default()

    jvm {
        jvmToolchain(8)
        withJava()

        testRuns["test"].executionTask.configure {
            useJUnitPlatform()
        }
    }

    js(IR) {
        moduleName = "log-appender-base"
        binaries.executable()

        browser {
            testTask {
                useKarma {
                    useChromeHeadless()
                    useFirefoxHeadless()
                }
            }
        }

        nodejs {
            testTask {
                useMocha {
                    timeout = "20s" // Mocha times out after 2 s, which is too short for bufferExceeded() test
                }
            }
        }
    }

    // wasm()


    linuxX64()
    mingwX64()

    ios {
        binaries {
            framework {
                baseName = "log-appender-base"
            }
        }
    }
    iosSimulatorArm64()
    macosX64()
    macosArm64()
    watchos()
    watchosSimulatorArm64()
    tvos()
    tvosSimulatorArm64()


    val coroutinesVersion: String by project
    val kotlinxDatetimeVersion: String by project
    val ktorVersion: String by project
    val kotestVersion: String by project

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")

                api("org.jetbrains.kotlinx:kotlinx-datetime:$kotlinxDatetimeVersion")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))

                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:$coroutinesVersion")

                implementation("io.kotest:kotest-assertions-core:$kotestVersion")
            }
        }

        val jvmMain by getting {
            dependencies {
                compileOnly("io.ktor:ktor-client-cio:$ktorVersion")
                compileOnly("com.fasterxml.jackson.module:jackson-module-kotlin:2.15.0")
            }
        }
        val jvmTest by getting

        val jsMain by getting
        val jsTest by getting

        val nativeMain by getting
        val nativeTest by getting
    }
}



ext["customArtifactId"] = "log-appender-base"

apply(from = "../gradle/scripts/publish-codinux.gradle.kts")
