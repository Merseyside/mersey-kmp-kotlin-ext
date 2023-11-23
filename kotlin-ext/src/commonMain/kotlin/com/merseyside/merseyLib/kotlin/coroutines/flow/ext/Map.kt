package com.merseyside.merseyLib.kotlin.coroutines.flow.ext

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*

fun <T, R> Flow<T>.mapState(
    scope: CoroutineScope,
    initialValue: R,
    started: SharingStarted = SharingStarted.Eagerly,
    transform: suspend (data: T) -> R
): StateFlow<R> {
    return distinctUntilChanged().map(transform).stateIn(scope, started, initialValue)
}

fun <T> Flow<T>.mapToList(): Flow<List<T>> {
    return map { listOf(it) }
}

fun <T, R> Flow<List<T>>.mapList(mapper: (T) -> R): Flow<List<R>> {
    return map { list -> list.map(mapper) }
}