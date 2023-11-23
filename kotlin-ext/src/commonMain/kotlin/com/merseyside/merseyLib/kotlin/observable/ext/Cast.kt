package com.merseyside.merseyLib.kotlin.observable.ext

import com.merseyside.merseyLib.kotlin.observable.Disposable
import com.merseyside.merseyLib.kotlin.observable.EventObservableField
import com.merseyside.merseyLib.kotlin.observable.ObservableField
import com.merseyside.merseyLib.kotlin.observable.SingleObservableEvent

fun <T> ObservableField<T>.toEventObservableField(): EventObservableField = object : SingleObservableEvent() {

    override fun addObserver(observer: Observer<Unit>): Disposable<Unit> {
        val disposable = super.addObserver(observer)

        val eventDisposable = this@toEventObservableField.observe(ignoreCurrent = true) {
            call()
        }

        return object : Disposable<Unit>() {
            override fun dispose(): Boolean {
                eventDisposable.dispose()
                return disposable.dispose()
            }

        }
    }
}