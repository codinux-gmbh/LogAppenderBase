package net.codinux.log

import kotlinx.coroutines.Dispatchers
import kotlin.concurrent.thread
import kotlin.coroutines.CoroutineContext

actual val Dispatchers.IOorDefault: CoroutineContext
    get() = Dispatchers.IO


internal actual object PlatformFunctions {

    actual fun addShutdownHook(action: () -> Unit) {
        Runtime.getRuntime().addShutdownHook(thread(start = false, name = "Shutdown Hook") {
            action()
        })
    }

}


internal actual object StdErr {

    actual fun println(message: String) {
        System.err.println(message)
    }

}