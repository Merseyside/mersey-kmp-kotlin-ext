package com.merseyside.merseyLib.kotlin.coroutines.queue

import com.merseyside.merseyLib.kotlin.coroutines.exception.NoParamsException
import com.merseyside.merseyLib.kotlin.coroutines.utils.defaultDispatcher
import com.merseyside.merseyLib.kotlin.coroutines.utils.uiDispatcher
import com.merseyside.merseyLib.kotlin.logger.ILogger
import com.merseyside.merseyLib.kotlin.logger.Logger
import kotlinx.coroutines.*

class CoroutineQueue<Result, Args>(
    var scope: CoroutineScope = CoroutineScope(defaultDispatcher)
) : ILogger {

    var fallOnException: Boolean = false

    val hasQueueWork: Boolean
        get() = workBuffer.isNotEmpty()

    private val asyncJob = SupervisorJob()
    private val workBuffer = ArrayDeque<Pair<suspend () -> Result, Args?>>()
    var job: Job? = null
        internal set

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

    suspend fun execute(): Boolean {
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

        return true
    }

    private suspend fun runSequentialWork() = coroutineScope {
        while (isActive && hasQueueWork) {
            val workPair = workBuffer.removeFirst()
            val deferred = startNewJobAsync(workPair.first)
            completeJob(deferred.await(), workPair.second)
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

    override val tag: String = "CoroutineQueue"
}