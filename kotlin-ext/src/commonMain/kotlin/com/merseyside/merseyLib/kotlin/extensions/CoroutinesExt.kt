package com.merseyside.merseyLib.kotlin.extensions

import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

fun <T> CoroutineScope.asyncIO(ioFun: () -> T) = async(Dispatchers.Default) { ioFun() }

fun CoroutineScope.launchDelayed(
    delayMillis: Long,
    context: CoroutineContext = EmptyCoroutineContext,
    start: CoroutineStart = CoroutineStart.DEFAULT,
    block: suspend CoroutineScope.() -> Unit
): Job = launch(context, start) {
    delay(delayMillis)
    block()
}

