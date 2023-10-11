package net.codinux.log

import ch.qos.logback.classic.Logger
import ch.qos.logback.classic.LoggerContext
import io.mockk.*
import kotlinx.datetime.Instant
import kotlinx.datetime.toJavaInstant
import net.codinux.log.config.LogAppenderConfig
import net.codinux.log.LogWriter
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory

class LogbackAppenderBaseTest {

    private val config = LogAppenderConfig()

    private val logWriterMock = mockk<LogWriter> {
        every { this@mockk.config } returns this@LogbackAppenderBaseTest.config
    }

    private val underTest = object : LogbackAppenderBase() {
        override fun createLogWriter(config: LogAppenderConfig) = logWriterMock
    }.apply {
        this.start()
    }

    private val log = LoggerFactory.getLogger(LogbackAppenderBaseTest::class.java)

    init {
        (LoggerFactory.getILoggerFactory() as? LoggerContext)?.let { context ->
            context.getLogger(Logger.ROOT_LOGGER_NAME).addAppender(underTest)
        }
    }


    @Test
    fun stop() {
        val message = "Just a test, no animals have been harmed"
        val exception = RuntimeException("As i said, just a test")

        val capturedInstant = slot<Instant>()
        val capturedLevel = slot<String>()
        val capturedMessage = slot<String>()
        val capturedException = slot<Throwable>()
        every { logWriterMock.writeRecord(capture(capturedInstant), capture(capturedLevel), capture(capturedMessage), any(), any(), capture(capturedException), any(), any(), any()) } just runs


        log.error(message, exception)


        assertThat(capturedMessage.captured).isEqualTo(message)
        assertThat(capturedException.captured).isEqualTo(exception)
        assertThat(capturedLevel.captured).isEqualTo("ERROR")
        assertThat(capturedInstant.captured.toJavaInstant()).isBetween(java.time.Instant.now().minusSeconds(1), java.time.Instant.now().plusSeconds(1))
    }

}