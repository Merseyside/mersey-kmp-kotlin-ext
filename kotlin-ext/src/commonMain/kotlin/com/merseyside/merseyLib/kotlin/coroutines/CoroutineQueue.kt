package com.merseyside.merseyLib.kotlin.coroutines

import com.merseyside.merseyLib.kotlin.logger.Logger
import com.merseyside.merseyLib.kotlin.coroutines.exception.NoParamsException
import com.merseyside.merseyLib.kotlin.coroutines.utils.uiDispatcher
import com.merseyside.merseyLib.kotlin.logger.ILogger
import kotlinx.coroutines.*

class CoroutineQueue<Result, Args>(
    var scope: CoroutineScope = CoroutineScope(uiDispatcher)
) : ILogger {

    var fallOnException: Boolean = false

    private val asyncJob = SupervisorJob()
    private val workBuffer = ArrayDeque<Pair<suspend () -> Result, Args?>>()
    private var job: Job? = null

    val isActive: Boolean
        get() = job?.isActive ?: false

    var onPreExecute: () -> Unit = {}
    var onComplete: (Result, Args?) -> Unit = { _, _ -> }
    var onError: (Throwable) -> Unit = {}
    var onPostExecute: () -> Unit = {}

    fun add(resultProvider: suspend () -> Result) {
        workBuffer.addLast(resultProvider to null)
    }

    fun add(args: Args?, resultProvider: suspend () -> Result) {
        workBuffer.addLast(resultProvider to args)
    }

    fun addAndExecute(block: suspend () -> Result): Job? {
        return addAndExecute(null, block)
    }

    fun addAndExecute(args: Args?, block: suspend () -> Result): Job? {
        add(args, resultProvider = block)

        return if (!isActive) {
            execute()
        } else null
    }

    fun execute(
        coroutineScope: CoroutineScope = scope,
        cancelIfAlreadyLaunched: Boolean = false
    ): Job? {
        if (!cancelIfAlreadyLaunched) {
            if (isActive) {
                Logger.logErr(
                    this, "Already launched! Cancel before current job " +
                            "before call execute method."
                )
                return null
            }
        }

        if (workBuffer.isEmpty()) {
            Logger.logErr(this, "No work added!")
            return null
        }

        return coroutineScope.launch {
            onPreExecute()

            try {
                runSequentialWork()
            } catch (exception: CancellationException) {
                Logger.logErr(this@CoroutineQueue, "The coroutine had canceled")
            } catch (exception: NoParamsException) {
                throw exception
            } catch (throwable: Throwable) {
                Logger.logErr("CoroutineQueue", throwable)
                if (fallOnException) throw throwable
                else onError(throwable)
            }

            onPostExecute()
        }.also { job = it }
    }

    private suspend fun runSequentialWork() = coroutineScope {
        while (isActive && workBuffer.isNotEmpty()) {
            val workPair = workBuffer.first()
            val deferred = startNewJobAsync(workPair.first)
            completeJob(deferred.await(), workPair.second)

            workBuffer.removeFirst()
        }
    }

    private suspend fun startNewJobAsync(resultProvider: suspend () -> Result): Deferred<Result> {
        return doWorkAsync(resultProvider)
    }

    private fun completeJob(result: Result, args: Args?) {
        onComplete(result, args)
    }

    private suspend fun doWorkAsync(block: suspend () -> Result) = coroutineScope {
        async(asyncJob) { block() }
    }

    override val tag: String = "CoroutineWorkManager"
}