package com.merseyside.merseyLib.kotlin.coroutines.ext

import com.merseyside.merseyLib.kotlin.entity.Result
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow

fun <T> StateFlow<Result<T>>.mapToValue(
    scope: CoroutineScope,
    started: SharingStarted = SharingStarted.WhileSubscribed()
): StateFlow<T?> {
    return this.mapState(
        scope = scope,
        started = started
    ) { it.value }
}