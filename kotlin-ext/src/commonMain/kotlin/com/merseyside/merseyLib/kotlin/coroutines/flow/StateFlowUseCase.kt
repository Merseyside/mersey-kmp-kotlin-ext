package com.merseyside.merseyLib.kotlin.coroutines.flow

import com.merseyside.merseyLib.kotlin.coroutines.utils.defaultDispatcher
import com.merseyside.merseyLib.kotlin.coroutines.utils.uiDispatcher
import com.merseyside.merseyLib.kotlin.logger.Logger
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.StateFlow

abstract class StateFlowUseCase<T, Params>(
    val coroutineScope: CoroutineScope = CoroutineScope(uiDispatcher)
) {

    lateinit var stateFlow: StateFlow<T>
        private set
    private val asyncJob = SupervisorJob()

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
        get() {
            return job?.isActive ?: false
        }

    protected open fun isInitialized(): Boolean {
        return this::stateFlow.isInitialized
    }

    open fun init(initialValue: T): StateFlow<T> {
        if (isInitialized()) {
            Logger.logErr("Already initialized!")
        } else {
            stateFlow = provideStateFlow(initialValue)
        }

        return stateFlow
    }

    protected abstract fun provideStateFlow(initialValue: T): StateFlow<T>

    private suspend fun doWorkDeferredAsync(
        params: Params? = null,
        block: suspend (Params?) -> T
    ): Deferred<T> = coroutineScope {
        async(asyncJob + defaultDispatcher) {
            block(params)
        }.also { job = it }
    }

    suspend fun collect(collector: FlowCollector<T>): Nothing {
        stateFlow.collect(collector)
    }

    protected suspend fun execute(params: Params? = null, block: suspend (Params?) -> T): T {
        return doWorkDeferredAsync(params, block).await()
    }
}
