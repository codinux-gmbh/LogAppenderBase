package net.codinux.log

import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

actual val Dispatchers.IOorDefault: CoroutineContext
    get() = Dispatchers.IO


internal actual object StdErr {

    actual fun println(message: String) {
        System.err.println(message)
    }

}