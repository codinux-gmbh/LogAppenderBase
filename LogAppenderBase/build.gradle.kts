@file:OptIn(ExperimentalKotlinGradlePluginApi::class)

import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.targets.js.dsl.ExperimentalWasmDsl

plugins {
    kotlin("multiplatform")
}


kotlin {
    compilerOptions {
        // suppresses compiler warning: [EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING] 'expect'/'actual' classes (including interfaces, objects, annotations, enums, and 'actual' typealiases) are in Beta.
        freeCompilerArgs.add("-Xexpect-actual-classes")

        // avoid "variable has been optimised out" in debugging mode
        if (System.getProperty("idea.debugger.dispatch.addr") != null) {
            freeCompilerArgs.add("-Xdebug")
        }
    }


    jvmToolchain(8)

    jvm {
        withJava()

        testRuns["test"].executionTask.configure {
            useJUnitPlatform()

            testLogging {
                showExceptions = true
                showStandardStreams = true
                events("passed", "skipped", "failed")
                // exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
            }
        }
    }

    js(IR) {
        moduleName = "log-appender-base"
        binaries.library()

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

    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        browser {
            testTask {
                useKarma {
                    useChromeHeadless()
                    useFirefoxHeadless()
                }
            }
        }
    }


    linuxX64()
    mingwX64()

    iosX64()
    iosArm64()
    iosSimulatorArm64()
    macosX64()
    macosArm64()
    watchosArm64()
    watchosSimulatorArm64()
    tvosArm64()
    tvosSimulatorArm64()

    applyDefaultHierarchyTemplate()


    val coroutinesVersion: String by project

    val kmpDateTimeVersion: String by project
    val slf4jVersion: String by project

    val assertKVersion: String by project

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")

                api("net.dankito.datetime:kmp-datetime:$kmpDateTimeVersion")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))

                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:$coroutinesVersion")

                implementation("com.willowtreeapps.assertk:assertk:$assertKVersion")
            }
        }

        val jvmMain by getting {
            dependencies {
                compileOnly("org.slf4j:slf4j-api:$slf4jVersion")
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
