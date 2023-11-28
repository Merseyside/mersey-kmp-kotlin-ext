package com.merseyside.merseyLib.kotlin.observable.ext

import com.merseyside.merseyLib.kotlin.observable.ObservableField

expect inline fun <reified F : ObservableField<T>, reified T> F.debounce(millis: Long): ObservableField<T>