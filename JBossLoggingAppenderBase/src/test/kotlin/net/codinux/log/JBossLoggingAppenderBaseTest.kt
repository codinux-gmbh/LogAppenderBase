package net.codinux.log

import io.mockk.*
import net.codinux.log.config.LoggedEventFields
import net.dankito.datetime.Instant
import net.dankito.datetime.toJavaInstant
import org.assertj.core.api.Assertions.assertThat
import org.jboss.logging.Logger
import org.junit.jupiter.api.Test

class JBossLoggingAppenderBaseTest {

    private val logWriterMock = mockk<LogWriter> {
        every { this@mockk.isEnabled } returns true
        every { this@mockk.loggedEventFields } returns LoggedEventFields.All
    }

    private val underTest = object : JBossLoggingAppenderBase(logWriterMock) { }

    private val log = Logger.getLogger(JBossLoggingAppenderBaseTest::class.java)

    init {
        JBossLoggingUtil.registerLogHandler(underTest)
    }


    @Test
    fun callToJBossLoggerCallsLogWriter() {
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