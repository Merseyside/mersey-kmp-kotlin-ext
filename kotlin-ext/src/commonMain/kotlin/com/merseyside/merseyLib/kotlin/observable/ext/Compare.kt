package com.merseyside.merseyLib.kotlin.observable.ext

import com.merseyside.merseyLib.kotlin.observable.ObservableField

/**
 * @return true if value and new value are equals, false otherwise
 */
fun <T> ObservableField<T>.compare(value: T?): Boolean {
    return this.value == value
}