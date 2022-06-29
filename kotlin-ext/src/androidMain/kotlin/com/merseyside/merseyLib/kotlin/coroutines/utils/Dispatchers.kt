package com.merseyside.merseyLib.kotlin.coroutines.utils

import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

actual val defaultDispatcher: CoroutineContext = Dispatchers.Default
actual val uiDispatcher: CoroutineContext = Dispatchers.Main