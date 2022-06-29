package com.merseyside.merseyLib.kotlin.coroutines.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

fun <T1, T2, R> combineState(
    flow1: StateFlow<T1>,
    flow2: StateFlow<T2>,
    scope: CoroutineScope,
    sharingStarted: SharingStarted = SharingStarted.Eagerly,
    transform: (T1, T2) -> R
): StateFlow<R> = combine(flow1, flow2) {
        o1, o2 -> transform.invoke(o1, o2)
}.stateIn(scope, sharingStarted, transform.invoke(flow1.value, flow2.value))

fun <T1, T2, T3, R> combineState(
    flow1: StateFlow<T1>,
    flow2: StateFlow<T2>,
    flow3: StateFlow<T3>,
    scope: CoroutineScope,
    sharingStarted: SharingStarted = SharingStarted.Eagerly,
    transform: (T1, T2, T3) -> R
): StateFlow<R> = combine(flow1, flow2, flow3) {
        o1, o2, o3 -> transform.invoke(o1, o2, o3)
}.stateIn(scope, sharingStarted, transform.invoke(flow1.value, flow2.value, flow3.value))

fun <T1, T2, T3, T4, R> combineState(
    flow1: StateFlow<T1>,
    flow2: StateFlow<T2>,
    flow3: StateFlow<T3>,
    flow4: StateFlow<T4>,
    scope: CoroutineScope,
    sharingStarted: SharingStarted = SharingStarted.Eagerly,
    transform: (T1, T2, T3, T4) -> R
): StateFlow<R> = combine(flow1, flow2, flow3, flow4) {
        o1, o2, o3, o4 -> transform.invoke(o1, o2, o3, o4)
}.stateIn(scope, sharingStarted, transform.invoke(flow1.value, flow2.value, flow3.value, flow4.value))