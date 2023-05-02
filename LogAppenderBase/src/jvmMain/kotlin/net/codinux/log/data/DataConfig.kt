package net.codinux.log.data

import java.time.Instant
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

private val startTimeFormat = DateTimeFormatter.ofPattern(DataConfig.StartTimeFormat)

fun DataConfig.getCurrentTimeFormatted(): String =
    Instant.now().atOffset(ZoneOffset.UTC).format(startTimeFormat)