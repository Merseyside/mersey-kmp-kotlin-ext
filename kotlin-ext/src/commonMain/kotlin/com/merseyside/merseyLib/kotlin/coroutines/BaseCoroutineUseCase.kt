package com.merseyside.merseyLib.kotlin.coroutines

import com.merseyside.merseyLib.kotlin.logger.ILogger
import com.merseyside.merseyLib.kotlin.logger.Logger
import kotlinx.coroutines.*

abstract class BaseCoroutineUseCase<T, Params>(
    protected val observingScope: CoroutineScope,
    private val executionStrategy: ExecutionStrategy
): ILogger {
    private val asyncJob = SupervisorJob()

    private var jobList: MutableList<Deferred<T>> = mutableListOf()

    val isActive: Boolean
        get() = jobList.any { job -> job.isActive }

    protected abstract suspend fun doWork(params: Params?): T

    internal suspend fun executeAsync(params: Params?): T = coroutineScope {
        if (executionStrategy == ExecutionStrategy.CANCEL_PREV_JOB) this@BaseCoroutineUseCase.cancel()

        async(asyncJob + Dispatchers.IO) {
            doWork(params)
        }.also { job -> jobList.add(job) }

    }.also { job -> job.invokeOnCompletion { jobList.remove(job) } }.await()

    fun cancel(cause: CancellationException? = null): Boolean {
        Logger.logErr(tag, "Cancel coroutine use case")
        val isActive = isActive
        jobList.forEach { job -> job.cancel(cause) }
        jobList.clear()
        return isActive
    }

    suspend operator fun invoke(params: Params? = null) = executeAsync(params)

    override val tag: String
        get() = this::class.simpleName ?: "UnknownCoroutineUseCase"
}

enum class ExecutionStrategy {
    CANCEL_PREV_JOB, INDEPENDENT_EXECUTION
}