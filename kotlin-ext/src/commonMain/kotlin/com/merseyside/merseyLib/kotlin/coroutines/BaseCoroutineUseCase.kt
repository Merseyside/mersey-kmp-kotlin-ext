package com.merseyside.merseyLib.kotlin.coroutines

import com.merseyside.merseyLib.kotlin.coroutines.utils.CompositeJob
import com.merseyside.merseyLib.kotlin.coroutines.utils.defaultDispatcher
import com.merseyside.merseyLib.kotlin.coroutines.utils.uiDispatcher
import kotlinx.coroutines.*

abstract class BaseCoroutineUseCase<T, Params>(var executionStrategy: ExecutionStrategy) {
    protected val mainScope: CoroutineScope by lazy { CoroutineScope(uiDispatcher) }
    private val asyncJob = SupervisorJob()

    private val compositeJob = CompositeJob()

    val isActive: Boolean
        get() = compositeJob.isActive

    protected abstract suspend fun doWork(params: Params?): T

    internal suspend fun executeAsync(params: Params?): T = coroutineScope {
        if (executionStrategy == ExecutionStrategy.CANCEL_PREV_JOB) compositeJob.cancel()

        compositeJob.add {
            async(asyncJob + defaultDispatcher) {
                doWork(params)
            }
        }
    }.await()

    fun cancel(cause: CancellationException? = null): Boolean {
        return compositeJob.cancel(cause)
    }

    suspend operator fun invoke(params: Params? = null) = executeAsync(params)
}

enum class ExecutionStrategy {
    CANCEL_PREV_JOB, INDEPENDENT_EXECUTION
}