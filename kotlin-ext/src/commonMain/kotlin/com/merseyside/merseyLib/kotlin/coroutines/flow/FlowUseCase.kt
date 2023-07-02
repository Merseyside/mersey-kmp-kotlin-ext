package com.merseyside.merseyLib.kotlin.coroutines.flow

import com.merseyside.merseyLib.kotlin.coroutines.exception.NoParamsException
import com.merseyside.merseyLib.kotlin.coroutines.utils.defaultDispatcher
import com.merseyside.merseyLib.kotlin.coroutines.utils.uiDispatcher
import com.merseyside.merseyLib.kotlin.logger.Logger
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

abstract class FlowUseCase<T, Params> {

    protected val mainScope: CoroutineScope by lazy { CoroutineScope(uiDispatcher) }

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
    protected abstract fun getFlow(params: Params?): Flow<T>

    @OptIn(ExperimentalCoroutinesApi::class)
    fun observe(
        coroutineScope: CoroutineScope = mainScope,
        params: Params? = null,
        onEmit: (T) -> Unit,
        onError: (Throwable) -> Unit = {}
    ): Job {
        val flow = getFlow(params)
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
            flow.collect { data ->
                onEmit(data)
            }
        }.also { job = it }
    }

    fun cancel() {
        job?.cancel()
        job = null
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(params: Params? = null): Flow<T> {
        return getFlow(params)
    }
}