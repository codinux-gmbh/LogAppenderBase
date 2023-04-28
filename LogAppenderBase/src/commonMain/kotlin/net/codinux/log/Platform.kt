package net.codinux.log

import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

internal expect val Dispatchers.IOorDefault: CoroutineContext