package com.merseyside.merseyLib.kotlin.coroutines

import com.merseyside.merseyLib.kotlin.Logger
import com.merseyside.merseyLib.kotlin.coroutines.exception.NoParamsException
import com.merseyside.merseyLib.kotlin.coroutines.utils.uiDispatcher
import kotlinx.coroutines.*

class CoroutineWorkManager<Result, Args>(
    private val mode: WorkerMode = WorkerMode.SEQUENTIAL,
    var scope: CoroutineScope = CoroutineScope(uiDispatcher)
) {
    enum class WorkerMode { SEQUENTIAL, PARALLEL }

    private val asyncJob = SupervisorJob()
    private val workBuffer = ArrayDeque<Pair<suspend () -> Result, Args>>()
    private val jobs: MutableList<Job> = ArrayList()

    val isActive: Boolean
        get() {
            return jobs.firstOrNull { it.isActive } != null
        }

    var onPreExecute: () -> Unit = {}
    var onComplete: (Result, Args) -> Unit = { _, _ ->}
    var onError: (Throwable) -> Unit = {}
    var onPostExecute: () -> Unit = {}

    fun add(args: Args, block: suspend () -> Result) {
        workBuffer.addLast(Pair(block, args))
    }

    fun addAndExecute(args: Args, block: suspend () -> Result): Job? {
        add(args, block = block)

        return if (mode == WorkerMode.PARALLEL) {
            execute()
        } else null
    }

    fun execute(
        coroutineScope: CoroutineScope = scope,
        cancelIfAlreadyLaunched: Boolean =  false
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
        }.also { jobs.add(it) }
    }

    private suspend fun runSequentialWork() = coroutineScope {
        while(isActive && workBuffer.isNotEmpty()) {
            val workPair = workBuffer.first()
            val deferred = doWorkAsync(workPair.first)
            completeJob(deferred.await(), workPair.second)

            workBuffer.removeFirst()
        }
    }

    private suspend fun runParallelWork() = coroutineScope {
        workBuffer.map {
            async {
                val deferred = doWorkAsync(it.first)
                completeJob(deferred.await(), it.second)
            }
        }

        workBuffer.clear()
    }

    private suspend fun completeJob(data: Result, args: Args) = coroutineScope {
        jobs.remove(coroutineContext.job)
        onComplete(data, args)
    }

    private suspend fun doWorkAsync(block: suspend () -> Result): Deferred<Result> = coroutineScope {
        async(asyncJob + Dispatchers.Default) {
            block()
        }
    }
}