package com.merseyside.merseyLib.kotlin.coroutines.flow

import com.merseyside.merseyLib.kotlin.entity.result.Result
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

abstract class ResultStateFlowUseCase<T, Params>(
    coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.Main),
    initialValue: Result<T> = Result.NotInitialized()
): MutableStateFlowUseCase<Result<T>, Params>(coroutineScope) {

    override suspend fun update(params: Params?): Result<T> {
        val loadingValue = getLoadingValue(params)

        updateValue(Result.Loading(loadingValue))
        return super.update(params)
    }

    override fun updateAsync(
        coroutineScope: CoroutineScope,
        params: Params?,
        onComplete: (Result<T>) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        coroutineScope.launch {
            try {
                onComplete(update(params))
            } catch(e: Throwable) {
                updateValue(Result.Error(e))
                onError(e)
            }
        }
    }

    override fun provideStateFlow(coroutineScope: CoroutineScope): MutableStateFlow<Result<T>> {
        return MutableStateFlow(Result.NotInitialized())
    }

    open fun getLoadingValue(params: Params?): T? {
        return null
    }
}