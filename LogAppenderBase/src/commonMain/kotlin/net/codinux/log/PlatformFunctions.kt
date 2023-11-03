package net.codinux.log

import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

internal expect val Dispatchers.IOorDefault: CoroutineContext


// do not name object `Platform` as this would conflict with net.codinux.log.Platform of kmp-log / klf
internal expect object PlatformFunctions {

    fun addShutdownHook(action: () -> Unit)

}

internal expect object StdErr {

    fun println(message: String)

}