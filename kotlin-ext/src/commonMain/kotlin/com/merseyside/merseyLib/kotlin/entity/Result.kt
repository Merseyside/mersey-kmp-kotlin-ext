package com.merseyside.merseyLib.kotlin.entity

sealed class Result<T> {

    abstract val value: T?

    fun isInitialized() = this !is NotInitialized
    fun isSuccess() = this is Success

    class NotInitialized<T>(override val value: T? = null): Result<T>()

    class Success<T>(override val value: T) : Result<T>()

    class Loading<T>(override val value: T? = null) : Result<T>()

    class Error<T>(
        val throwable: Throwable? = null,
        override val value: T? = null,
    ) : Result<T>()
}
