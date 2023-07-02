@file:OptIn(ExperimentalCoroutinesApi::class)

package com.merseyside.merseyLib.kotlin.coroutines.ext

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*

fun <T, R> StateFlow<T>.mapState(
    scope: CoroutineScope,
    started: SharingStarted = SharingStarted.Eagerly,
    transform: (data: T) -> R
): StateFlow<R> {
    return mapLatest { transform(it) }
        .stateIn(scope, started, transform(value))
}

fun <T, R> StateFlow<T>.mapState(
    scope: CoroutineScope,
    initialValue: R,
    started: SharingStarted = SharingStarted.Eagerly,
    transform: suspend (data: T) -> R
): StateFlow<R> {
    return mapLatest { transform(it) }
        .stateIn(scope, started, initialValue)
}

fun <T> StateFlow<T>.filterState(
    scope: CoroutineScope,
    started: SharingStarted = SharingStarted.Eagerly,
    predicate: suspend (data: T) -> Boolean
): StateFlow<T> {
    return filter { predicate(it) }
        .stateIn(scope, started, value)
}

fun <T> StateFlow<T?>.filterNotNullState(
    scope: CoroutineScope,
    started: SharingStarted = SharingStarted.Eagerly,
    ifNullBlock: () -> T
): StateFlow<T> {
    return filterNotNull().stateIn(scope, started, value ?: ifNullBlock())
}