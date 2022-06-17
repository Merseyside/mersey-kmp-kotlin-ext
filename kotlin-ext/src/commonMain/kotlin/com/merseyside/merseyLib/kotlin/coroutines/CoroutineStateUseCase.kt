package com.merseyside.merseyLib.kotlin.coroutines

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn

abstract class CoroutineStateUseCase<T, Params> : CoroutineScope by CoroutineScope(uiDispatcher) {

    protected abstract suspend fun doWork(params: Params?): T

    fun toStateFlow(
        params: Params?,
        scope: CoroutineScope = this,
        started: SharingStarted = SharingStarted.WhileSubscribed(),
        initialValue: T
    ): StateFlow<Result<T>> {
        return flow {
            val result = try {
                Result.Success(doWork(params))
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
}

sealed class Result<T> {

    abstract val value: T?

    class Loading<T>(
        override val value: T? = null
    ) : Result<T>()

    class Error<T>(
        val throwable: Throwable? = null,
        override val value: T? = null,
    ) : Result<T>()

    class Success<T>(override val value: T) : Result<T>()
}

