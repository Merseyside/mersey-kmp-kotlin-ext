package com.merseyside.merseyLib.kotlin.coroutines

import com.merseyside.merseyLib.kotlin.Logger
import com.merseyside.merseyLib.kotlin.coroutines.exception.NoParamsException
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlin.coroutines.CoroutineContext

abstract class FlowUseCase<T, Params> : CoroutineScope by CoroutineScope(uiDispatcher) {
    var job: Job? = null
        set(value) {
            field?.let {
                if (it.isActive) {
                    it.cancel()
                }
            }

            field = value
        }

    @ExperimentalCoroutinesApi
    protected abstract fun executeOnBackground(params: Params?): Flow<T>

    @OptIn(ExperimentalCoroutinesApi::class)
    fun observe(
        coroutineScope: CoroutineScope = this,
        params: Params? = null,
        onEmit: (T) -> Unit,
        onError: (Throwable) -> Unit = {}
    ): Job {
        val flow = executeOnBackground(params)
            .onEach { data -> onEmit.invoke(data) }
            .catch { cause ->
                when (cause) {
                    is CancellationException -> Logger.log(
                        this@FlowUseCase,
                        "Coroutine had canceled"
                    )
                    is NoParamsException -> {
                        Logger.log(this@FlowUseCase, "No params passed!")
                        throw cause
                    }
                    else -> {
                        Logger.logErr(cause)
                        onError.invoke(cause)
                    }
                }
            }.flowOn(defaultDispatcher)

        return coroutineScope.launch {
            flow.collect()
        }.also { job = it }
    }

    fun cancel() {
        job?.cancel()
        job = null
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(params: Params? = null): Flow<T> {
        return executeOnBackground(params)
    }
}