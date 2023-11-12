package net.codinux.log

import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.classic.spi.IThrowableProxy
import ch.qos.logback.classic.spi.ThrowableProxy
import kotlinx.datetime.Instant
import kotlinx.datetime.toKotlinInstant
import net.codinux.log.config.LogAppenderConfig

abstract class LogbackAppenderBase(
    config: LogAppenderConfig = LogAppenderConfig()
) : ConfigurableUnsynchronizedAppenderBase(config) {

    protected var logWriter: LogWriter? = null

    protected open val eventSupportsInstant: Boolean = try {
        // starting from Logback 1.3.x ILoggingEvent has an instant field / getInstant() method
        ILoggingEvent::class.java.getDeclaredMethod("getInstant")
        true
    } catch (ignored: Exception) {
        false
    }


    abstract fun createLogWriter(config: LogAppenderConfig): LogWriter

    override fun start() {
        // config is now loaded -> LogWriter can be created / started
        if (config.enabled) {
            this.logWriter = createLogWriter(config)
        }

        super.start()
    }

    override fun append(event: ILoggingEvent?) {
        if (config.enabled && event != null) {
            logWriter?.writeRecord(
                if (eventSupportsInstant) event.instant.toKotlinInstant() else Instant.fromEpochMilliseconds(event.timeStamp),
                event.level.levelStr,
                event.formattedMessage,
                if (fields.logsLoggerName) event.loggerName else null,
                if (fields.logsThreadName) event.threadName else null,
                if (fields.logsException) getThrowable(event) else null,
                if (fields.logsMdc) event.mdcPropertyMap else null,
                if (fields.logsMarker) event.marker?.name else null
            )
        }
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
            logWriter?.stateLogger?.error("Could not get Throwable from IThrowableProxy", e)
        }

        return null
    }

}