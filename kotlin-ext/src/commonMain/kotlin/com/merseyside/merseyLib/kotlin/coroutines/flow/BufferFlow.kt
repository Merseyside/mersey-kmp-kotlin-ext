package com.merseyside.merseyLib.kotlin.coroutines.flow

import com.merseyside.merseyLib.kotlin.coroutines.ext.onCancel
import com.merseyside.merseyLib.kotlin.coroutines.timer.CountDownTimer
import com.merseyside.merseyLib.kotlin.coroutines.timer.CountDownTimerListener
import com.merseyside.merseyLib.kotlin.extensions.isNotZero
import com.merseyside.merseyLib.kotlin.extensions.isZero
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BufferFlow<T>(
    private val coroutineScope: CoroutineScope,
    private val capacity: Int = 1,
    private val timeoutMs: Long = 0L
) : Flow<List<T>> {

    private val timer = if (timeoutMs.isNotZero()) CountDownTimer(scope = coroutineScope).apply {
        setCountDownTimerListener(object : CountDownTimerListener {
            override fun onTick(timeLeft: Long, error: Exception?) {}

            override fun onStop(timeLeft: Long, error: Exception?) {
                if (timeLeft.isZero()) {
                    coroutineScope.launch { internalEmit(list) }
                }
            }
        })
    } else null

    private val list: MutableList<T> = ArrayList()
    private val sharedFlow = MutableSharedFlow<List<T>>()

    init {
        coroutineScope.onCancel {
            withContext(NonCancellable) {
                if (list.isNotEmpty()) {
                    internalEmit(list)
                }
            }
        }
    }

    fun emit(vararg values: T) {
        list.addAll(values.toList())

        if (list.size >= capacity) {
            timer?.stopTimer()
            coroutineScope.launch {
                internalEmit(list)
            }
        } else {
            timer?.restartTimer(timeoutMs)
        }
    }

    private suspend fun internalEmit(list: MutableList<T>) {
        sharedFlow.emit(list)
        list.clear()
    }

    override suspend fun collect(collector: FlowCollector<List<T>>): Nothing {
        sharedFlow.collect(collector)
    }
}