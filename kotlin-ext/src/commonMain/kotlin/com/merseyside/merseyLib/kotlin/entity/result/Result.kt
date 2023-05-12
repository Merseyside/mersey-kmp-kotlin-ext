package com.merseyside.merseyLib.kotlin.entity.result

sealed class Result<T> {

    abstract val value: T?

    class NotInitialized<T>(override val value: T? = null): Result<T>()

    class Success<T>(override val value: T) : Result<T>()

    class Loading<T>(override val value: T? = null) : Result<T>()

    class Error<T>(
        val throwable: Throwable? = null,
        override val value: T? = null,
    ) : Result<T>() {
        override fun toString(): String {
            return StringBuilder(super.toString()).run {
                appendLine()
                append(throwable?.stackTraceToString() ?: "No exception provided")
                toString()
            }
        }
    }

    override fun toString(): String {
        return StringBuilder().run {
            append(this@Result::class.simpleName).append(" ")
            append("value = ")
            append("$value")
            toString()
        }
    }
}
