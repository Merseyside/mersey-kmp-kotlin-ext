package com.merseyside.merseyLib.kotlin.observable.ext

import android.os.Handler
import android.os.Looper
import com.merseyside.merseyLib.kotlin.observable.MutableObservableField
import com.merseyside.merseyLib.kotlin.observable.ObservableField
import com.merseyside.merseyLib.kotlin.observable.SingleObservableEvent
import com.merseyside.merseyLib.kotlin.utils.safeLet

actual inline fun <reified T> ObservableField<T>.debounce(millis: Long): ObservableField<T> {

    val runnable: Runnable
    val mutableObservableField = if (T::class == Unit::class) {
        val temp = SingleObservableEvent()
        runnable = Runnable { temp.call() }
        temp
    } else {
        val temp = MutableObservableField(value)
        runnable = Runnable { temp.value = this.value }
        temp
    }

    val looper = Looper.myLooper()
    safeLet(looper) {
        val handler = Handler(it)
        this.observe { _ ->
            handler.removeCallbacks(runnable)
            handler.postDelayed(runnable, millis)
        }

    }

    return mutableObservableField as ObservableField<T>
}