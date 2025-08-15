package net.codinux.log

import ch.qos.logback.classic.Logger
import ch.qos.logback.classic.LoggerContext
import ch.qos.logback.classic.spi.ILoggingEvent
import net.codinux.log.config.LoggedEventFields
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import java.util.concurrent.atomic.AtomicInteger

class DisabledLogbackAppenderBaseTest {

    private val countCalls = AtomicInteger(0)

    private val underTest = object : LogbackAppenderBase() {
        override fun createLogWriter(): LogWriter? = null
        override fun getLoggedEventFields(): LoggedEventFields? = null

        override fun appendEvent(logWriter: LogWriter, event: ILoggingEvent) {
            countCalls.incrementAndGet()
        }
    }.apply {
        this.start()
    }

    private val log = LoggerFactory.getLogger(DisabledLogbackAppenderBaseTest::class.java)

    init {
        (LoggerFactory.getILoggerFactory() as? LoggerContext)?.let { context ->
            context.getLogger(Logger.ROOT_LOGGER_NAME).addAppender(underTest)
        }
    }


    @Test
    fun disabled_NothingGetsWrittenToLogWriter() {
        log.error("Any")

        assertThat(countCalls.get()).isEqualTo(0)
    }

}