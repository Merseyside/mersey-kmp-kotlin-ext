package com.merseyside.merseyLib.kotlin.coroutines.flow

import com.merseyside.merseyLib.kotlin.logger.ILogger
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.StateFlow

abstract class StateFlowUseCase<T, Params>(
    val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.Main)
): ILogger {

    private val asyncJob = SupervisorJob()

    val stateFlow: StateFlow<T> by lazy {
        provideStateFlow(coroutineScope)
    }

    open val value: T
        get() = stateFlow.value

    var job: Job? = null
        set(value) {
            field?.let {
                if (it.isActive) {
                    it.cancel()
                }
            }
            field = value
        }

    val isActive: Boolean
        get() = job?.isActive ?: false

    protected abstract fun provideStateFlow(coroutineScope: CoroutineScope): StateFlow<T>

    private suspend fun doWorkDeferredAsync(
        params: Params? = null,
        block: suspend (Params?) -> T
    ): Deferred<T> = coroutineScope {
        async(asyncJob + Dispatchers.IO) {
            block(params)
        }.also { job = it }
    }

    suspend fun collect(collector: FlowCollector<T>): Nothing {
        stateFlow.collect(collector)
    }

    protected suspend fun execute(params: Params? = null, block: suspend (Params?) -> T): T {
        return doWorkDeferredAsync(params, block).await()
    }

    override val tag: String = "StateFlowUseCase"
}
