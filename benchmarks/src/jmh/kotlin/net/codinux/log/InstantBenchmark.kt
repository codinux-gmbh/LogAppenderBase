package net.codinux.log

import org.openjdk.jmh.annotations.Benchmark
import org.openjdk.jmh.annotations.BenchmarkMode
import org.openjdk.jmh.annotations.Mode
import java.time.Instant

fun main(args: Array<String>) {
    org.openjdk.jmh.Main.main(args)
}

@BenchmarkMode(Mode.Throughput)
open class InstantBenchmark { // Benchmark classes should not be final

    companion object {
        private const val SecondsSinceEpoch = 1696968288L
        private const val NanosecondsOfSecond = 9077000L

        private val InstantForTest = Instant.ofEpochSecond(SecondsSinceEpoch, NanosecondsOfSecond)

        private val Expected = "$SecondsSinceEpoch${NanosecondsOfSecond.toString().padStart(9, '0')}"
    }

    @Benchmark
    fun arithmetic() {
        val result = InstantForTest.epochSecond * 1_000_000_000L + InstantForTest.nano

        check(result.toString() == Expected)
    }

    @Benchmark
    fun string() {
        val result = "${InstantForTest.epochSecond}${InstantForTest.nano.toString().padStart(9, '0')}"

        check(result == Expected)
    }
}