package com.merseyside.merseyLib.kotlin.coroutines.ext

import com.merseyside.merseyLib.kotlin.coroutines.BaseCoroutineUseCase
import com.merseyside.merseyLib.kotlin.entity.Result
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*

fun <T, Params> BaseCoroutineUseCase<T, Params>.toFlow(
    params: () -> Params? = {null}
): Flow<Result<T>> {
    return flow {
        emit(Result.Loading())
        val result = try {
            Result.Success(this@toFlow.invoke(params()))
        } catch (e: Exception) {
            Result.Error(e)
        }

        emit(result)
    }
}

fun <T, Params> BaseCoroutineUseCase<T, Params>.toStateFlow(
    scope: CoroutineScope,
    started: SharingStarted = SharingStarted.WhileSubscribed(),
    initialValue: T? = null,
    params: () -> Params? = {null}
): StateFlow<Result<T>> {
    return toFlow(params).stateIn(
        scope,
        started,
        Result.NotInitialized(initialValue)
    )
}