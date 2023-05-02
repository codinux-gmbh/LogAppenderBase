package net.codinux.log

import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

actual val Dispatchers.IOorDefault: CoroutineContext
    get() = Dispatchers.Default


internal actual object StdErr {

    actual fun println(message: String) {
        kotlin.io.println(message) // TODO: how to write to stderr in Kotlin native?
    }

}