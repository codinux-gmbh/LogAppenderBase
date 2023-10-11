package net.codinux.log

import org.jboss.logmanager.LogManager
import org.jboss.logmanager.formatters.ColorPatternFormatter
import org.jboss.logmanager.formatters.PatternFormatter
import org.jboss.logmanager.handlers.ConsoleHandler
import java.util.logging.Formatter
import java.util.logging.Handler
import java.util.logging.SimpleFormatter

object JBossLoggingUtil {

    fun useJBossLoggingAsJavaUtilLoggingManager() {
        System.setProperty("java.util.logging.manager", "org.jboss.logmanager.LogManager")
    }

    fun registerLogHandler(vararg handler: Handler) {
        LogManager.getLogManager().addHandlerToRootLogger(*handler)
    }

    fun consoleHandler(formatter: Formatter = SimpleFormatter()) =
        ConsoleHandler(formatter)

    fun patternConsoleHandler() =
        consoleHandler(PatternFormatter())

    fun colorConsoleHandler() =
        consoleHandler(ColorPatternFormatter())

}