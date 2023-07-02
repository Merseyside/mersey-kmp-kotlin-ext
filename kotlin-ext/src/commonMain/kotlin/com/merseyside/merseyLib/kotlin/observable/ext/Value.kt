package com.merseyside.merseyLib.kotlin.observable.ext

import com.merseyside.merseyLib.kotlin.observable.MutableObservableField
import com.merseyside.merseyLib.kotlin.observable.ObservableField

fun <T> ObservableField<T>.valueNotNull(): T {
    return value ?: throw NullPointerException("Required value is not null!")
}

fun <T> MutableObservableField<T>.compareAndSetNullable(newValue: T?): Boolean {
    return if (!compare(newValue)) {
        value = newValue
        true
    } else false
}

@Throws(NullPointerException::class)
fun <T> MutableObservableField<T>.compareAndSet(newValue: T): Boolean {
    return if (!compare(newValue)) {
        value = newValue
        true
    } else false
}