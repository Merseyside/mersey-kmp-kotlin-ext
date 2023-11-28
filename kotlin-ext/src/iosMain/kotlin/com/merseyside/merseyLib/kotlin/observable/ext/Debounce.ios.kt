package com.merseyside.merseyLib.kotlin.observable.ext

import com.merseyside.merseyLib.kotlin.observable.ObservableField

actual inline fun < reified F : ObservableField<T>, reified T> F.debounce(millis: Long): ObservableField<T> {
    TODO("Not yet implemented")
}