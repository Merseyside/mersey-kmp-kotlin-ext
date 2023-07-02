package com.merseyside.merseyLib.kotlin.entity.result

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

/**
 * Creates new result because value is immutable property.
 */
fun <T> Result<T>.update(update: (value: T?) -> Result<T>): Result<T> {
    return update(value)
}

fun <T> Result<T>.valueOr(provider: () -> T): T {
    return value ?: provider()
}

fun <T> Result<List<T>>.valueOrEmpty(): List<T> {
    return valueOr { emptyList() }
}

fun <K, T> Result<Map<K, T>>.valueOrEmpty(): Map<K, T> {
    return valueOr { emptyMap() }
}

fun <T> Result<T>.successOrNull(): Result.Success<T>? {
    return if (isSuccess()) this else null
}

inline fun <T> Result<T>.isInitialized() = this !is Result.NotInitialized

@OptIn(ExperimentalContracts::class)
inline fun <T> Result<T>.isSuccess(): Boolean {
    contract {
        returns(true) implies (this@isSuccess is Result.Success<T>)
    }

    return this is Result.Success<T>
}

@OptIn(ExperimentalContracts::class)
inline fun <T> Result<T>.isLoading(): Boolean {
    contract {
        returns(true) implies (this@isLoading is Result.Loading<T>)
    }

    return this is Result.Loading<T>
}

@OptIn(ExperimentalContracts::class)
inline fun <T> Result<T>.isError(): Boolean {
    contract {
        returns(true) implies (this@isError is Result.Error<T>)
    }

    return this is Result.Error<T>
}

fun <T> Result<T>.isEmpty(): Boolean {
    return when (value) {
        null -> true
        is Collection<*> -> {
            (value as Collection<*>).isEmpty()
        }

        else -> false
    }
}
