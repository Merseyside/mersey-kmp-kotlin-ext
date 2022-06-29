package com.merseyside.merseyLib.kotlin.coroutines.utils

import com.merseyside.merseyLib.kotlin.coroutines.exception.NoParamsException
import kotlinx.coroutines.delay

inline fun <Params, Result> requireParams(
    params: Params?,
    block: Params.() -> Result
): Result {
    return if (params != null) {
        block(params)
    } else throw NoParamsException()
}

suspend fun debounce(
    waitMs: Long = 300L,
    destinationFunction: suspend () -> Unit
) {
    delay(waitMs)
    destinationFunction.invoke()
}