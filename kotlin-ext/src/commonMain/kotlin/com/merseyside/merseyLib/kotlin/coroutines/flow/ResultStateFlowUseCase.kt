package com.merseyside.merseyLib.kotlin.coroutines.flow

import com.merseyside.merseyLib.kotlin.coroutines.utils.uiDispatcher
import kotlinx.coroutines.CoroutineScope
import com.merseyside.merseyLib.kotlin.entity.Result
import com.merseyside.merseyLib.kotlin.logger.Logger
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

abstract class ResultStateFlowUseCase<T, Params>(
    coroutineScope: CoroutineScope = CoroutineScope(uiDispatcher),
    initialValue: Result<T> = Result.NotInitialized()
): MutableStateFlowUseCase<Result<T>, Params>(coroutineScope) {

    init {
        init(initialValue)
    }

    override fun isInitialized(): Boolean {
        return value.isInitialized()
    }

    override suspend fun update(params: Params?): Result<T> {
        val loadingValue = getLoadingValue(params)

        updateValue(Result.Loading(loadingValue))
        return super.update(params)
    }

    override fun update(
        coroutineScope: CoroutineScope,
        params: Params?,
        onError: (Throwable) -> Unit
    ) {
        coroutineScope.launch {
            try {
                update(params)
            } catch(e: Throwable) {
                updateValue(Result.Error(e))
                onError(e)
            }
        }
    }

    final override fun init(initialValue: Result<T>): StateFlow<Result<T>> {
        if (!isInitialized() && initialValue.isInitialized()) {
            updateValue(initialValue)
        } else {
            Logger.logErr("Already initialized or passed not valid result")
        }

        return stateFlow
    }

    override fun updateValue(value: Result<T>): Result<T> {
        return if (value.isInitialized()) {
            super.updateValue(value)
        } else throw IllegalArgumentException("")
    }

    open fun getLoadingValue(params: Params?): T? {
        return null
    }

}