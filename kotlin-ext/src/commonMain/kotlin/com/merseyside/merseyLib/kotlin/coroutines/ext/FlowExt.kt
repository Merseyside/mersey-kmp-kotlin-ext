package com.merseyside.merseyLib.kotlin.coroutines.ext

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*

fun <T, R> Flow<T>.mapState(
    scope: CoroutineScope,
    initialValue: R,
    started: SharingStarted = SharingStarted.Eagerly,
    transform: suspend (data: T) -> R
): StateFlow<R> {
    return map(transform).stateIn(scope, started, initialValue)
}