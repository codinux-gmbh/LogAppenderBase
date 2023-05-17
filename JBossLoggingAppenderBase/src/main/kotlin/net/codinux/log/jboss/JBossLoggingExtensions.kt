package net.codinux.log.jboss

import java.util.logging.Handler
import java.util.logging.LogManager
import java.util.logging.Logger


fun LogManager.getRootLogger() =
    this.getLogger("")

fun LogManager.addHandlerToRootLogger(vararg handlers: Handler) {
    this.getRootLogger().addHandler(*handlers)
}

fun Logger.addHandler(vararg handlers: Handler) {
    handlers.forEach { handler ->
        this.addHandler(handler)
    }
}