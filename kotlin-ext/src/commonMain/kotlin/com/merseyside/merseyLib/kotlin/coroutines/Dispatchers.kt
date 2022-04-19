package com.merseyside.merseyLib.kotlin.coroutines

import kotlin.coroutines.CoroutineContext

expect val defaultDispatcher: CoroutineContext
expect val uiDispatcher: CoroutineContext