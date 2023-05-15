package net.codinux.log.extensions

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.isActive
import net.codinux.log.statelogger.AppenderStateLogger


val <T> Channel<T>.isNotEmpty
    get() = isEmpty == false

fun CoroutineScope.cancelSafely(stateLogger: AppenderStateLogger? = null) {
    try {
        if (this.isActive) {
            this.cancel()
        }
    } catch (e: Throwable) {
        stateLogger?.error("Could not cancel CoroutineScope", e)
    }
}