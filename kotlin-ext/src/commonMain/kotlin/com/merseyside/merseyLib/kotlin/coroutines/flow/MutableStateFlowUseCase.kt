package com.merseyside.merseyLib.kotlin.coroutines.flow

import com.merseyside.merseyLib.kotlin.coroutines.utils.uiDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.updateAndGet
import kotlinx.coroutines.launch

abstract class MutableStateFlowUseCase<T, Params>(
    coroutineScope: CoroutineScope = CoroutineScope(uiDispatcher)
) : StateFlowUseCase<T, Params>(coroutineScope) {

    private lateinit var mutStateFlow: MutableStateFlow<T>

    protected abstract suspend fun updateWithParams(params: Params?): T

    final override fun provideStateFlow(initialValue: T): MutableStateFlow<T> {
        return MutableStateFlow(initialValue).also { mutStateFlow = it }
    }

    suspend fun update(params: Params? = null): T {
        val value = executeAsync(params) {
            updateWithParams(params)
        }

        return mutStateFlow.updateAndGet { value }
    }

    fun update(
        coroutineScope: CoroutineScope = this.coroutineScope,
        params: Params? = null,
        onUpdated: (T) -> Unit = {},
        onError: (Throwable) -> Unit = {}
    ) {
        coroutineScope.launch {
            try {
                onUpdated(update(params))
            } catch(e: Throwable) {
                onError(e)
            }
        }
    }

    fun initAndUpdate(
        coroutineScope: CoroutineScope = this.coroutineScope,
        initialValue: T,
        params: Params? = null,
        onUpdated: (T) -> Unit,
        onError: (Throwable) -> Unit = {}
    ): StateFlow<T> {
        return init(initialValue).also {
            update(coroutineScope, params, onUpdated, onError)
        }
    }

    operator fun invoke(initialValue: T): StateFlow<T> {
        return init(initialValue)
    }
}