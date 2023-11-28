package com.merseyside.merseyLib.kotlin.observable.ext

import android.os.Handler
import android.os.Looper
import com.merseyside.merseyLib.kotlin.observable.Disposable
import com.merseyside.merseyLib.kotlin.observable.MutableObservableField
import com.merseyside.merseyLib.kotlin.observable.ObservableField

actual inline fun <reified F : ObservableField<T>, reified T> F.debounce(millis: Long): ObservableField<T> =
    object : MutableObservableField<T>() {
        override fun addObserver(observer: Observer<T>): Disposable<T> {
            val disposable = super.addObserver(observer)

            val runnable = Runnable {
                value = if (T::class == Unit::class) Unit as T
                else this@debounce.value
            }

            val looper = Looper.getMainLooper()
            val handler = Handler(looper)
            val sourceDisposable = this@debounce.observe { _ ->
                handler.removeCallbacks(runnable)
                handler.postDelayed(runnable, millis)
            }

            return object : Disposable<T>() {
                override fun dispose(): Boolean {
                    sourceDisposable.dispose()
                    return disposable.dispose()
                }
            }
        }
    }