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

    override var value: T
        get() = stateFlow.value
        set(value) {
            mutStateFlow.value = value
        }

    protected abstract suspend fun updateWithParams(params: Params?): T

    final override fun provideStateFlow(initialValue: T): MutableStateFlow<T> {
        return MutableStateFlow(initialValue).also { mutStateFlow = it }
    }

    open suspend fun update(params: Params? = null): T {
        val value = execute(params) {
            updateWithParams(it)
        }

        return updateValue(value)
    }

    open fun update(
        coroutineScope: CoroutineScope = this.coroutineScope,
        params: Params? = null,
        onError: (Throwable) -> Unit = {}
    ) {
        coroutineScope.launch {
            try {
                update(params)
            } catch(e: Throwable) {
                onError(e)
            }
        }
    }

    open fun initAndUpdate(
        coroutineScope: CoroutineScope = this.coroutineScope,
        initialValue: T,
        params: Params? = null,
        onError: (Throwable) -> Unit = {}
    ): StateFlow<T> {
        return init(initialValue).also {
            update(coroutineScope, params, onError)
        }
    }

    open fun updateValue(value: T): T {
        return mutStateFlow.updateAndGet { value }
    }

    operator fun invoke(initialValue: T): StateFlow<T> {
        return init(initialValue)
    }
}