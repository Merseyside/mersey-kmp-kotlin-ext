package com.merseyside.merseyLib.kotlin.coroutines.ext

import com.merseyside.merseyLib.kotlin.coroutines.BaseCoroutineUseCase
import com.merseyside.merseyLib.kotlin.coroutines.exception.NoParamsException
import com.merseyside.merseyLib.kotlin.coroutines.flow.Result
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn

fun <T, Params> BaseCoroutineUseCase<T, Params>.toStateFlow(
    scope: CoroutineScope,
    started: SharingStarted = SharingStarted.WhileSubscribed(),
    initialValue: T? = null,
    params: () -> Params? = {null}
): StateFlow<Result<T>> {
    return flow {
        val result = try {
            Result.Success(this@toStateFlow.invoke(params()))
        } catch (e: Exception) {
            Result.Error(e)
        }

        emit(result)
    }.stateIn(
        scope,
        started,
        Result.Loading(initialValue)
    )
}