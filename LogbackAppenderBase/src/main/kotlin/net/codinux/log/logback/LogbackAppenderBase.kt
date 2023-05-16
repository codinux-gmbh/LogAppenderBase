package net.codinux.log.logback

import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.classic.spi.IThrowableProxy
import ch.qos.logback.classic.spi.ThrowableProxy
import ch.qos.logback.core.UnsynchronizedAppenderBase
import kotlinx.datetime.Instant
import net.codinux.log.LogWriter
import net.codinux.log.statelogger.AppenderStateLogger
import net.codinux.log.statelogger.StdOutStateLogger

open class LogbackAppenderBase(
    protected open val isAppenderEnabled: Boolean,
    protected open val logWriter: LogWriter,
    protected open val stateLogger: AppenderStateLogger = StdOutStateLogger()
) : UnsynchronizedAppenderBase<ILoggingEvent>() {

    override fun append(event: ILoggingEvent?) {
        if (isAppenderEnabled && event != null) {
            logWriter.writeRecord(
                Instant.fromEpochMilliseconds(event.timeStamp),
                event.level.levelStr,
                event.formattedMessage,
                if (logWriter.config.logsLoggerName) event.loggerName else null,
                if (logWriter.config.logsThreadName) event.threadName else null,
                if (logWriter.config.logsException) getThrowable(event) else null,
                if (logWriter.config.logsMdc) event.mdcPropertyMap else null,
                if (logWriter.config.logsMarker) event.marker?.name else null
            )
        }
    }


    override fun stop() {
        logWriter.close()

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
            stateLogger.error("Could not get Throwable from IThrowableProxy", e)
        }

        return null
    }

}