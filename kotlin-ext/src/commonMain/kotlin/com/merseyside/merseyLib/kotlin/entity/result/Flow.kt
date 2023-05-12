package com.merseyside.merseyLib.kotlin.entity.result

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.map

fun <T> Flow<Result<T>>.filterSuccess(): Flow<Result.Success<T>> {
    return filterIsInstance()
}

fun <T> Flow<Result<T>>.filterSuccessValues(): Flow<T> {
    return filterSuccess().map { it.value }
}