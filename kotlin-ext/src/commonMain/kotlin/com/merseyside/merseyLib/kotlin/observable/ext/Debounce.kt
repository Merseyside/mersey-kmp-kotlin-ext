package com.merseyside.merseyLib.kotlin.observable.ext

import com.merseyside.merseyLib.kotlin.observable.ObservableField

expect inline fun <reified T> ObservableField<T>.debounce(millis: Long): ObservableField<T>