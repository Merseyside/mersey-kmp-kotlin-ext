package com.merseyside.merseyLib.kotlin.coroutines.flow

import com.merseyside.merseyLib.kotlin.coroutines.utils.uiDispatcher
import com.merseyside.merseyLib.kotlin.logger.Logger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.updateAndGet
import kotlinx.coroutines.launch

abstract class MutableStateFlowUseCase<T, Params>(
    coroutineScope: CoroutineScope = CoroutineScope(uiDispatcher),
    startWhenCreated: Boolean = false
) : StateFlowUseCase<T, Params>(coroutineScope) {

    private val mutStateFlow: MutableStateFlow<T>
        get() = stateFlow as MutableStateFlow<T>

    override var value: T
        get() = stateFlow.value
        set(value) {
            mutStateFlow.value = value
        }

    init {
        if (startWhenCreated) {
            coroutineScope.launch {
                provideStateFlow()
            }
        }
    }

    protected abstract suspend fun updateWithParams(params: Params?): T

    abstract override fun provideStateFlow(): MutableStateFlow<T>

    open suspend fun update(params: Params? = null): T {
        val value = execute(params) {
            updateWithParams(it)
        }

        return updateValue(value)
    }

    open fun updateAsync(
        coroutineScope: CoroutineScope = this.coroutineScope,
        params: Params? = null,
        onComplete: (newValue: T) -> Unit = {},
        onError: (Throwable) -> Unit = {}
    ) {
        coroutineScope.launch {
            try {
                onComplete(update(params))
            } catch(e: Throwable) {
                Logger.logErr(e)
                onError(e)
            }
        }
    }

    internal fun updateValue(value: T): T {
        return mutStateFlow.updateAndGet { value }
    }
}