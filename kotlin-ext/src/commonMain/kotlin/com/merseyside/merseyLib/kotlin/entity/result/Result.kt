package com.merseyside.merseyLib.kotlin.entity.result

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
sealed class Result<T> {

    abstract val value: T?

    @Serializable
    class NotInitialized<T>(override val value: T? = null): Result<T>()

    @Serializable
    class Success<T>(override val value: T? = null) : Result<T>() {
        fun requireValue() = requireNotNull(value)
    }

    @Serializable
    class Loading<T>(override val value: T? = null) : Result<T>()

    @Serializable
    class Error<T>(
        @Transient val throwable: Throwable? = null,
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
