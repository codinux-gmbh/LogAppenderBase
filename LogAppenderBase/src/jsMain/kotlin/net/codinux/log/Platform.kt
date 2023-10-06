package net.codinux.log

import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

actual val Dispatchers.IOorDefault: CoroutineContext
    get() = Dispatchers.Default


internal actual object Platform {

    actual fun addShutdownHook(action: () -> Unit) {
        // TODO: register window.onClosing event listener
    }

}


internal actual object StdErr {

    actual fun println(message: String) {
        console.error(message)
    }

}