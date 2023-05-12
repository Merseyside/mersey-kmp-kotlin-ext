package com.merseyside.merseyLib.kotlin.observable.ext

import com.merseyside.merseyLib.kotlin.observable.ObservableField

fun <T> ObservableField<T>.valueNotNull(): T {
    return value ?: throw NullPointerException("Required value is not null!")
}