plugins {
    kotlin("jvm")
    id("me.champeau.jmh")
}

val jmhDepVersion = "1.37"

dependencies {
    jmh("org.openjdk.jmh:jmh-kotlin-benchmark-archetype:$jmhDepVersion")
    jmh("org.openjdk.jmh:jmh-generator-annprocess:$jmhDepVersion")

    // this is the line that solves the missing /META-INF/BenchmarkList error
    jmhAnnotationProcessor("org.openjdk.jmh:jmh-generator-annprocess:$jmhDepVersion")

    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

jmh {
    jmhVersion = jmhDepVersion

    warmupIterations = 2

//    benchmarkMode = [ "Throughput" ]
    fork = 1
    iterations = 2
    jmhTimeout = "30s"

    resultFormat = "CSV"
}

tasks.test {
    useJUnitPlatform()
}