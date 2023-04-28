package net.codinux.log

import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

actual val Dispatchers.IOorDefault: CoroutineContext
    get() = Dispatchers.Default