package com.merseyside.merseyLib.kotlin.coroutines.flow.ext

import com.merseyside.merseyLib.kotlin.coroutines.ext.onCancel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

fun <T> Flow<T>.bufferUntilTimeout(capacity: Int, timeout: Long): Flow<List<T>> = channelFlow {
    check(capacity > 0) {
        "Capacity must be more than zero!"
    }

    val bufferedValues = mutableListOf<T>()
    var timerJob: Job? = null
    val mutex = Mutex()

    suspend fun clear() = mutex.withLock {
        bufferedValues.clear()
    }

    suspend fun bufferValue(value: T) = mutex.withLock {
        bufferedValues.add(value)
    }

    suspend fun emitValues() {
        send(bufferedValues.toList())
        clear()
        timerJob?.cancel()
    }

    fun startTimer(): Job {
        return launch {
            delay(timeout)
            emitValues()
        }
    }

    launch {
        this@bufferUntilTimeout.collect { value ->
//            check(bufferedValues.isEmpty() && timerJob?.isActive != true) { // simple check
//                "Something went wrong!"
//            }

            bufferValue(value)
            if (bufferedValues.size == capacity) {
                emitValues()
            } else {
                timerJob?.cancel()
                timerJob = startTimer()
            }
        }
    }

    coroutineScope {
        onCancel {
            withContext(NonCancellable) {
                if (bufferedValues.isNotEmpty()) {
                    emitValues()
                }
            }
        }
    }
}