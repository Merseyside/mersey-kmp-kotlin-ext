package com.merseyside.merseyLib.kotlin.coroutines.ext

import com.merseyside.merseyLib.kotlin.coroutines.BaseCoroutineUseCase
import com.merseyside.merseyLib.kotlin.entity.result.Result
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn

fun <T, Params> BaseCoroutineUseCase<T, Params>.toResultFlow(
    params: () -> Params? = {null}
): Flow<Result<T>> {
    return flow {
        emit(Result.Loading())
        val result = try {
            Result.Success(this@toResultFlow.invoke(params()))
        } catch (e: Exception) {
            Result.Error(e)
        }

        emit(result)
    }
}

fun <T, Params> BaseCoroutineUseCase<T, Params>.toResultStateFlow(
    scope: CoroutineScope,
    started: SharingStarted = SharingStarted.WhileSubscribed(),
    initialValue: T? = null,
    params: () -> Params? = {null}
): StateFlow<Result<T>> {
    return toResultFlow(params).stateIn(
        scope,
        started,
        Result.NotInitialized(initialValue)
    )
}

fun <T, Params> BaseCoroutineUseCase<T, Params>.toFlow(
    lazyParams: () -> Params? = { null }
): Flow<T> {
    return flow {
        val result = this@toFlow.invoke(lazyParams())
        emit(result)
    }
}

fun <T, Params> BaseCoroutineUseCase<T, Params>.toFlow(
    params: Params? = null
): Flow<T> {
    return flow {
        val result = this@toFlow.invoke(params)
        emit(result)
    }
}

fun <T, Params> BaseCoroutineUseCase<T, Params>.toStateFlow(
    scope: CoroutineScope,
    started: SharingStarted = SharingStarted.WhileSubscribed(),
    initialValue: T?,
    params: () -> Params? = {null}
): StateFlow<T?> {
    return toFlow(params).stateIn(
        scope,
        started,
        initialValue
    )
}

