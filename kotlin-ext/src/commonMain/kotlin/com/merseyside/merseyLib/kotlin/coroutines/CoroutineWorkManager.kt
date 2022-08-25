package com.merseyside.merseyLib.kotlin.coroutines

import com.merseyside.merseyLib.kotlin.logger.Logger
import com.merseyside.merseyLib.kotlin.coroutines.exception.NoParamsException
import com.merseyside.merseyLib.kotlin.coroutines.utils.uiDispatcher
import com.merseyside.merseyLib.kotlin.logger.ILogger
import kotlinx.coroutines.*

class CoroutineWorkManager<Result, Args>(
    private val mode: WorkerMode = WorkerMode.SEQUENTIAL,
    var scope: CoroutineScope = CoroutineScope(uiDispatcher)
): ILogger {
    enum class WorkerMode { SEQUENTIAL, PARALLEL }

    private val asyncJob = SupervisorJob()
    private val workBuffer = ArrayDeque<Pair<suspend () -> Result, Args?>>()
    private val jobs: MutableList<Job> = ArrayList()

    val isActive: Boolean
        get() = jobs.isNotEmpty()

    var onPreExecute: () -> Unit = {}
    var onComplete: (Result, Args?) -> Unit = { _, _ ->}
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

        return if (mode == WorkerMode.PARALLEL || !isActive) {
            execute()
        } else null
    }

    fun execute(
        coroutineScope: CoroutineScope = scope,
        cancelIfAlreadyLaunched: Boolean = false
    ): Job? {
        if (!cancelIfAlreadyLaunched && mode == WorkerMode.SEQUENTIAL) {
            if (isActive) {
                Logger.logErr(this, "Already launched! Cancel before current job " +
                        "before call execute method.")
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
                if (mode == WorkerMode.SEQUENTIAL) {
                    runSequentialWork()
                } else {
                    runParallelWork()
                }

            } catch (exception: CancellationException) {
                Logger.logErr(this@CoroutineWorkManager, "The coroutine had canceled")
            } catch (exception: NoParamsException) {
                throw exception
            } catch (throwable: Throwable) {
                Logger.logErr(throwable)
                onError(throwable)
                throwable.printStackTrace()
            }

            onPostExecute()
        }
    }

    private suspend fun runSequentialWork() = coroutineScope {
        while(isActive && workBuffer.isNotEmpty()) {
            val workPair = workBuffer.first()
            val deferred = startNewJobAsync(workPair.first)
            completeJob(awaitForResult(deferred), workPair.second)

            workBuffer.removeFirst()
        }
    }

    private suspend fun runParallelWork() = coroutineScope {
        workBuffer.forEach { (resultProvider, args) ->
            launch {
                val deferred = startNewJobAsync(resultProvider)
                completeJob(awaitForResult(deferred), args)
            }
        }

        workBuffer.clear()
    }

    private suspend fun startNewJobAsync(resultProvider: suspend () -> Result): Deferred<Result> {
        val deferred = doWorkAsync(resultProvider)
        jobs.add(deferred)
        return deferred
    }

    private suspend fun awaitForResult(deferred: Deferred<Result>): Result {
        return deferred.await().also {
            jobs.remove(deferred)
        }
    }

    private fun completeJob(result: Result, args: Args?) {
        onComplete(result, args)
    }

    private suspend fun doWorkAsync(block: suspend () -> Result): Deferred<Result> = coroutineScope {
        async(asyncJob) { block() }
    }

    override val tag: String = "CoroutineWorkManager"
}