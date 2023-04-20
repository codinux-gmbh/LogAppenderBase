package net.codinux.log.logback

import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.classic.spi.IThrowableProxy
import ch.qos.logback.classic.spi.ThrowableProxy
import ch.qos.logback.core.UnsynchronizedAppenderBase
import kotlinx.datetime.Instant
import net.codinux.log.LogRecord
import net.codinux.log.LogWriter
import java.net.InetAddress

open class LogbackAppenderBase(
    protected open val isAppenderEnabled: Boolean,
    protected open val logExceptions: Boolean = true,
    protected open val logWriter: LogWriter
) : UnsynchronizedAppenderBase<ILoggingEvent>() {

    protected open val hostName: String? = InetAddress.getLocalHost().hostName

    override fun append(eventObject: ILoggingEvent?) {
        if (isAppenderEnabled && eventObject != null) {
            logWriter.writeRecord(mapRecord(eventObject))
        }
    }

    protected open fun mapRecord(event: ILoggingEvent): LogRecord {
        return LogRecord(event.formattedMessage, Instant.fromEpochMilliseconds(event.timeStamp), event.level.levelStr, event.loggerName,
            event.threadName, getThrowable(event), hostName, event.mdcPropertyMap, event.marker?.name)
    }


    override fun stop() {
        logWriter.close()

        super.stop()
    }


    protected open fun getThrowable(event: ILoggingEvent): Throwable? {
        if (logExceptions) {
            event.throwableProxy?.let { proxy ->
                if (proxy is ThrowableProxy) {
                    return proxy.throwable
                } else {
                    return tryToInstantiateThrowable(proxy)
                }
            }
        }

        return null
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
            // TODO: log with error handler
//            errorHandler.logError("Could not get Throwable from IThrowableProxy", e)
        }

        return null
    }

}