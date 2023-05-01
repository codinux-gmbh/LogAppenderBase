package net.codinux.log.extensions

import java.time.Instant

val Instant.microAndNanosecondsPart: Long
    get() = this.nano % 1_000_000L