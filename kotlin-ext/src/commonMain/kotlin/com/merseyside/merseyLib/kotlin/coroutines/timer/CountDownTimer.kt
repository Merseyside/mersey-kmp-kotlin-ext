package com.merseyside.merseyLib.kotlin.coroutines.timer

/*
* https://github.com/Kotlin/kotlinx.coroutines/issues/2171
 */

import com.merseyside.merseyLib.kotlin.logger.Logger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.*
import kotlinx.coroutines.withContext

class CountDownTimer(
    private val delay: Long = 1000L,
    private val scope: CoroutineScope = CoroutineScope(Job() + Dispatchers.Unconfined)
) {

    private var listener: CountDownTimerListener? = null

    private var timerJob: Job? = null
    private var timerValue: Long = 0L

    var state: CurrentTimerState = CurrentTimerState.STOPPED
        private set(value) {
            if (field == CurrentTimerState.DESTROYED) {
                return
            }
            field = value
        }

    fun setCountDownTimerListener(listener: CountDownTimerListener) {
        this.listener = listener
    }

    fun startTimer(value: Long) {
        timerValue = value
        when (state) {
            CurrentTimerState.RUNNING -> {
                listener?.onTick(value, TimerException(TimerErrorTypes.ALREADY_RUNNING))
            }
            CurrentTimerState.PAUSED -> {
                listener?.onTick(value, TimerException(TimerErrorTypes.CURRENTLY_PAUSED))
            }
            CurrentTimerState.DESTROYED -> {
                listener?.onTick(value, TimerException(TimerErrorTypes.DESTROYED))
            }
            else -> {
                timerCanStart()
            }
        }
    }

    fun stopTimer() {
        val error = if (state == CurrentTimerState.STOPPED) {
            TimerException(TimerErrorTypes.NO_TIMER_RUNNING)
        } else {
            null
        }
        timerJob?.cancel()
        state = CurrentTimerState.STOPPED
        listener?.onStop(timerValue, error)
    }

    fun restartTimer(value: Long) {
        if (state == CurrentTimerState.DESTROYED) {
            throw IllegalStateException("Timer destroyed!")
        }

        if (state == CurrentTimerState.RUNNING) {
            stopTimer()
        }

        startTimer(value)
    }

    fun pauseTimer() {
        if (state == CurrentTimerState.PAUSED) {
            Logger.logErr(TAG, "Already paused, check your code for multiple callers")
        }
        state = CurrentTimerState.PAUSED
        timerJob?.cancel()
        listener?.onPause(timerValue)
    }

    fun continueTimer() {
        if (state == CurrentTimerState.RUNNING) {
            Logger.logErr(TAG, "Already running, check your code for multiple callers")
        }
        state = CurrentTimerState.RUNNING
        listener?.onContinue()
        timerCanStart()
    }

    fun destroyTimer() {
        scope.cancel("Timer was now destroyed. Need a new instance to work")
        listener?.onDestroy()
        state = CurrentTimerState.DESTROYED
    }

    private fun timerCanStart() {
        timerJob = scope.launch {

            state = CurrentTimerState.RUNNING

            onTick(timerValue)

            if (timerValue < delay) {
                delay(timerValue)
            } else {
                delay(delay)
            }

            timerLoop@ while (isActive) {
                timerValue -= delay

                if (timerValue <= 0L) {
                    state = CurrentTimerState.STOPPED

                    onTick(0L)
                    timerJob?.cancel()
                    listener?.onStop(0L)
                }
                else {
                    onTick(timerValue)

                    if (timerValue < delay) {
                        delay(timerValue)
                    } else {
                        delay(delay)
                    }
                }
            }
        }
    }

    private suspend fun onTick(timeLeft: Long, error: Exception? = null) =
        withContext(Dispatchers.Main) {
            listener?.onTick(timeLeft, error)
        }

    companion object {
        const val TAG = "CoroutineTimer"
    }
}

interface CountDownTimerListener {
    fun onTick(timeLeft: Long, error: Exception? = null)
    fun onStop(timeLeft: Long, error: Exception? = null)
    fun onContinue() {}
    fun onPause(remainingTime: Long) {}
    fun onDestroy() {}
}

enum class CurrentTimerState {
    RUNNING, PAUSED, STOPPED, DESTROYED
}

enum class TimerErrorTypes(val message: String) {
    ALREADY_RUNNING("This instance of the timer is already running, create a new instance or stop your current one"),
    CURRENTLY_PAUSED("This timer is currently paused. Choose to continue or stop to start over"),
    NO_TIMER_RUNNING("You are trying to stop or pause a timer that isn't running"),
    DESTROYED("This timer is destroyed and can't be used anymore")
}

private class TimerException(val type: TimerErrorTypes) : Exception(type.message)