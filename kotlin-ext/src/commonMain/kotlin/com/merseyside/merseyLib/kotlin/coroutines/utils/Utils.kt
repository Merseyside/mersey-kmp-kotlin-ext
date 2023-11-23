package com.merseyside.merseyLib.kotlin.coroutines.utils

import com.merseyside.merseyLib.kotlin.coroutines.exception.NoParamsException
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive

inline fun <Params, Result> requireParams(
    params: Params?,
    block: Params.() -> Result
): Result {
    return if (params != null) {
        block(params)
    } else throw NoParamsException()
}

suspend fun debounce(
    waitMs: Long,
    destinationFunction: suspend () -> Unit
) {
    delay(waitMs)
    destinationFunction.invoke()
}

suspend fun repeatUntilCancel(
    delay: Long = 0,
    repeatBlock: suspend () -> Unit
) = coroutineScope {
    while (isActive) {
        delay(delay)
        repeatBlock()
    }
}