package com.merseyside.merseyLib.kotlin.coroutines.ext

import com.merseyside.merseyLib.kotlin.coroutines.utils.derived.DerivedStateFlow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*

fun <T1, R> StateFlow<T1>.mapState(transform: (a: T1) -> R): StateFlow<R> {
    return DerivedStateFlow(
        getValue = { transform(this.value) },
        flow = this.map { a -> transform(a) }
    )
}

fun <T, R> StateFlow<T>.mapState(
    scope: CoroutineScope,
    started: SharingStarted = SharingStarted.Eagerly,
    transform: (data: T) -> R
): StateFlow<R> {
    return map { transform(it) }
        .stateIn(scope, started, transform(value))
}

fun <T> StateFlow<T>.filterState(
    scope: CoroutineScope,
    started: SharingStarted = SharingStarted.Eagerly,
    predicate: suspend (data: T) -> Boolean
): StateFlow<T> {
    return filter(predicate).stateIn(scope, started, value)
}

fun <T> StateFlow<T?>.filterNotNullState(
    scope: CoroutineScope,
    started: SharingStarted = SharingStarted.Eagerly,
    ifNullBlock: () -> T
): StateFlow<T> {
    return filterNotNull().stateIn(scope, started, value ?: ifNullBlock())
}

// Combine
fun <T1, T2, R> combineState(
    scope: CoroutineScope,
    flow1: StateFlow<T1>,
    flow2: StateFlow<T2>,
    sharingStarted: SharingStarted = SharingStarted.Eagerly,
    transform: (T1, T2) -> R
): StateFlow<R> = combine(flow1.drop(1), flow2.drop(1)) {
        o1, o2 -> transform.invoke(o1, o2)
}.stateIn(scope, sharingStarted, transform.invoke(flow1.value, flow2.value))

fun <T1, T2, T3, R> combineState(
    scope: CoroutineScope,
    flow1: StateFlow<T1>,
    flow2: StateFlow<T2>,
    flow3: StateFlow<T3>,
    sharingStarted: SharingStarted = SharingStarted.Eagerly,
    transform: (T1, T2, T3) -> R
): StateFlow<R> = combine(flow1.drop(1), flow2.drop(1), flow3.drop(1)) {
        o1, o2, o3 -> transform.invoke(o1, o2, o3)
}.stateIn(scope, sharingStarted, transform.invoke(flow1.value, flow2.value, flow3.value))

fun <T1, T2, T3, T4, R> combineState(
    scope: CoroutineScope,
    flow1: StateFlow<T1>,
    flow2: StateFlow<T2>,
    flow3: StateFlow<T3>,
    flow4: StateFlow<T4>,
    sharingStarted: SharingStarted = SharingStarted.Eagerly,
    transform: (T1, T2, T3, T4) -> R
): StateFlow<R> = combine(flow1.drop(1), flow2.drop(1), flow3.drop(1), flow4.drop(1)) {
        o1, o2, o3, o4 -> transform.invoke(o1, o2, o3, o4)
}.stateIn(scope, sharingStarted, transform.invoke(flow1.value, flow2.value, flow3.value, flow4.value))