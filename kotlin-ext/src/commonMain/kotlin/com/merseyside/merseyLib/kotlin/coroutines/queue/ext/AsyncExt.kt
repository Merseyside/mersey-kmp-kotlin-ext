package com.merseyside.merseyLib.kotlin.coroutines.queue.ext

import com.merseyside.merseyLib.kotlin.coroutines.queue.CoroutineQueue
import com.merseyside.merseyLib.kotlin.logger.Logger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

fun <Result, Args> CoroutineQueue<Result, Args>.addAndExecuteAsync(block: suspend () -> Result): Job? {
    return addAndExecuteAsync(null, block)
}

fun <Result, Args> CoroutineQueue<Result, Args>.addAndExecuteAsync(args: Args?, block: suspend () -> Result): Job? {
    add(args, resultProvider = block)
    return executeAsync()
}

fun <Result, Args> CoroutineQueue<Result, Args>.executeAsync(
    coroutineScope: CoroutineScope = scope,
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

    return coroutineScope.launch {
        execute()
    }.also { job = it }
}