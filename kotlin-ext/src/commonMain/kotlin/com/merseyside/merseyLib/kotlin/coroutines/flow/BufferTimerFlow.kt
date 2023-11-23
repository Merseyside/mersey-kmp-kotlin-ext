package com.merseyside.merseyLib.kotlin.coroutines.flow

import com.merseyside.merseyLib.kotlin.coroutines.ext.onCancel
import com.merseyside.merseyLib.kotlin.coroutines.timer.CountDownTimer
import com.merseyside.merseyLib.kotlin.coroutines.timer.CountDownTimerListener
import com.merseyside.merseyLib.kotlin.extensions.isNotZero
import com.merseyside.merseyLib.kotlin.extensions.isZero
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Use this flow when you want to achieve collecting data in a buffer with preassigned
 * @param[capacity] and release it when timer is over.
 * Timer restarts when new data emits
 */

class BufferTimerFlow<T>(
    private val coroutineScope: CoroutineScope,
    private val capacity: Int = 1,
    private val timeoutMs: Long = 0L
) : Flow<List<T>> {

    private val timer = if (timeoutMs.isNotZero()) CountDownTimer(scope = coroutineScope).apply {
        setCountDownTimerListener(object : CountDownTimerListener {
            override fun onTick(timeLeft: Long, error: Exception?) {}

            override fun onStop(timeLeft: Long, error: Exception?) {
                if (timeLeft.isZero()) {
                    coroutineScope.launch { internalEmit() }
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
                    internalEmit()
                }
            }
        }
    }

    fun emit(vararg values: T) {
        list.addAll(values.toList())

        if (list.size >= capacity) {
            timer?.stopTimer()
            coroutineScope.launch {
                internalEmit()
            }
        } else {
            timer?.restartTimer(timeoutMs)
        }
    }

    private suspend fun internalEmit() {
        sharedFlow.buffer()
        sharedFlow.emit(list.toList())
        list.clear()
    }

    override suspend fun collect(collector: FlowCollector<List<T>>): Nothing {
        sharedFlow.collect(collector)
    }
}