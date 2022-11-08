package com.merseyside.merseyLib.kotlin.coroutines.flow

import com.merseyside.merseyLib.kotlin.coroutines.utils.uiDispatcher
import com.merseyside.merseyLib.kotlin.entity.Result
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

abstract class ResultStateFlowUseCase<T, Params>(
    coroutineScope: CoroutineScope = CoroutineScope(uiDispatcher),
    initialValue: Result<T> = Result.NotInitialized()
): MutableStateFlowUseCase<Result<T>, Params>(coroutineScope) {

    init {
        init(initialValue)
    }

    override fun isInitialized(): Boolean {
        return super.isInitialized() && value.isInitialized()
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

    override fun updateValue(value: Result<T>): Result<T> {
        return if (value.isInitialized()) {
            super.updateValue(value)
        } else throw IllegalArgumentException("")
    }

    open fun getLoadingValue(params: Params?): T? {
        return null
    }

}