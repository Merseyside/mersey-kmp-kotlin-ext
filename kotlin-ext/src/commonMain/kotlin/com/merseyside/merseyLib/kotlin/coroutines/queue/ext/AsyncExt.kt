package com.merseyside.merseyLib.kotlin.coroutines.queue.ext

import com.merseyside.merseyLib.kotlin.coroutines.queue.CoroutineQueue
import com.merseyside.merseyLib.kotlin.logger.Logger
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

fun <Result, Args> CoroutineQueue<Result, Args>.addAndExecuteAsync(
    coroutineContext: CoroutineContext = EmptyCoroutineContext,
    block: suspend () -> Result
): Job? {
    return addAndExecuteAsync(null, coroutineContext, block)
}

fun <Result, Args> CoroutineQueue<Result, Args>.addAndExecuteAsync(
    args: Args?,
    coroutineContext: CoroutineContext = EmptyCoroutineContext,
    block: suspend () -> Result
): Job? {
    add(args, resultProvider = block)
    return executeAsync(coroutineContext)
}

fun <Result, Args> CoroutineQueue<Result, Args>.executeAsync(
    coroutineContext: CoroutineContext = EmptyCoroutineContext,
    cancelIfAlreadyLaunched: Boolean = false
): Job? {
    if (!cancelIfAlreadyLaunched) {
        if (isActive) {
            Logger.logErr(
                this, "Already launched!"
            )
            return null
        }
    }

    if (!hasQueueWork) {
        Logger.logErr(this, "No work added!")
        return null
    }

    return scope.launch(coroutineContext) {
        execute()
    }.also { job = it }
}