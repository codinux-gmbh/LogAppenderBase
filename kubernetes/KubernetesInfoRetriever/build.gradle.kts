plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
}


kotlin {
    jvm {
        jvmToolchain(8)
        withJava()
        testRuns["test"].executionTask.configure {
            useJUnitPlatform()

        }
    }


    val ktorVersion: String by project
    val coroutinesVersion: String by project
    val kotlinSerializationVersion: String by project
    val kotestVersion: String by project

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("net.codinux.log:log-appender-base:$version")

                implementation("io.ktor:ktor-client-cio:$ktorVersion")
                implementation("io.ktor:ktor-client-content-negotiation:$ktorVersion")
                implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")

                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")

                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$kotlinSerializationVersion")
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

            }
        }
        val jvmTest by getting
    }
}