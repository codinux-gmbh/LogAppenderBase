package net.codinux.log

import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.classic.spi.IThrowableProxy
import ch.qos.logback.classic.spi.ThrowableProxy
import ch.qos.logback.core.UnsynchronizedAppenderBase
import net.codinux.log.config.LogAppenderConfig
import net.codinux.log.config.LoggedEventFields
import net.dankito.datetime.Instant
import net.dankito.datetime.toKmpInstant
import kotlin.time.Duration.Companion.minutes

/**
 * Base class for Logback appenders that delegate writing to a [LogWriter].
 *
 * Logback sets configuration directly on Appender class. So i turned the logic around:
 * I freed this class from config specific methods, so that its only dependency is `LogWriter`.
 *
 * If you're fine with default [LogAppenderConfig], simply derive your class from [ConfigurableLogbackAppenderBase].
 * If you have a custom configuration class, derive directly from this class and provide
 * setters for your config class fields.
 */
abstract class LogbackAppenderBase : UnsynchronizedAppenderBase<ILoggingEvent>() {

    /**
     * Return null if this appender is not enabled. Nothing gets logged then.
     */
    abstract fun createLogWriter(): LogWriter?


    protected var logWriter: LogWriter? = null

    protected open val eventSupportsInstant: Boolean = try {
        // starting from Logback 1.3.x ILoggingEvent has an instant field / getInstant() method
        ILoggingEvent::class.java.getDeclaredMethod("getInstant")
        true
    } catch (_: Throwable) {
        false
    }


    override fun start() {
        // config is now loaded -> LogWriter can be created / started (if enabled)
        this.logWriter = createLogWriter()

        super.start()
    }

    override fun append(event: ILoggingEvent?) {
        logWriter?.let { writer ->
            if (writer.isEnabled && event != null) {
                appendEvent(writer, event)
            }
        }
    }

    protected open fun appendEvent(logWriter: LogWriter, event: ILoggingEvent) {
        val fields = logWriter.loggedEventFields

        logWriter.writeRecord(
            if (eventSupportsInstant) event.instant.toKmpInstant() else Instant.ofEpochMilli(event.timeStamp),
            event.level.levelStr,
            event.formattedMessage,
            if (fields.logsLoggerName) event.loggerName else null,
            if (fields.logsThreadName) event.threadName else null,
            if (fields.logsException) getThrowable(event) else null,
            if (fields.logsMdc) event.mdcPropertyMap else null,
            if (fields.logsMarker) event.marker?.name else null
        )
    }


    override fun stop() {
        logWriter?.close()

        super.stop()
    }


    protected open fun getThrowable(event: ILoggingEvent): Throwable? {
        return event.throwableProxy?.let { proxy ->
            if (proxy is ThrowableProxy) {
                return proxy.throwable
            } else {
                return tryToInstantiateThrowable(proxy)
            }
        }
    }

    protected open fun tryToInstantiateThrowable(proxy: IThrowableProxy): Throwable? {
        try {
            val throwableClass = Class.forName(proxy.className)

            val throwable = throwableClass.declaredConstructors.firstOrNull { it.parameterCount == 1 && it.parameterTypes[0] == String::class.java }?.let { constructor ->
                constructor.newInstance(proxy.message) as Throwable
            } ?: throwableClass.getDeclaredConstructor().newInstance() as Throwable

            throwable.stackTrace = proxy.stackTraceElementProxyArray.map { it.stackTraceElement }.toTypedArray()

            return throwable
        } catch (e: Exception) {
            logWriter?.stateLogger?.error("Could not get Throwable from IThrowableProxy", e, 5.minutes)
        }

        return null
    }

}