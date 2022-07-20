package com.merseyside.merseyLib.kotlin.coroutines.ext

import com.merseyside.merseyLib.kotlin.logger.Logger
import com.merseyside.merseyLib.kotlin.coroutines.BaseCoroutineUseCase
import kotlinx.coroutines.*

fun <R1, P1, R2, P2> CoroutineScope.zipUseCases(
    c1: BaseCoroutineUseCase<R1, P1>,
    c2: BaseCoroutineUseCase<R2, P2>,
    p1: P1? = null,
    p2: P2? = null,
    onError: (error: Throwable) -> Unit = {},
    onComplete: (R1, R2) -> Unit = { _, _ -> },
): Job {
    return launch {
        val deferred1 = async { c1(p1) }
        val deferred2 = async { c2(p2) }

        try {
            onComplete(deferred1.await(), deferred2.await())
        } catch (throwable: Throwable) {
            onError(throwable)
            Logger.logErr(throwable)
        }
    }
}

fun <R1, P1, R2, P2, R3, P3> CoroutineScope.zipUseCases(
    c1: BaseCoroutineUseCase<R1, P1>,
    c2: BaseCoroutineUseCase<R2, P2>,
    c3: BaseCoroutineUseCase<R3, P3>,
    p1: P1? = null,
    p2: P2? = null,
    p3: P3? = null,
    onError: (error: Throwable) -> Unit = {},
    onComplete: (R1, R2, R3) -> Unit = { _, _, _ -> },
): Job {
    return launch {
        val deferred1 = async { c1(p1) }
        val deferred2 = async { c2(p2) }
        val deferred3 = async { c3(p3) }

        try {
            onComplete(deferred1.await(), deferred2.await(), deferred3.await())
        } catch (throwable: Throwable) {
            onError(throwable)
            Logger.logErr(throwable)
        }
    }
}