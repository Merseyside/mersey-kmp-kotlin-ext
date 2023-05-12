package com.merseyside.merseyLib.kotlin.observable.ext

import com.merseyside.merseyLib.kotlin.observable.MutableObservableField
import com.merseyside.merseyLib.kotlin.utils.safeLet


/**
 * Updates observable field with providing current value.
 * @return true if current and new values are not equals, false otherwise.
 */
fun <T> MutableObservableField<T>.update(update: (current: T?) -> T?): Boolean {
    val newValue = update(value)
    return if (newValue != value) {
        value = newValue
        true
    } else false
}

fun <T> MutableObservableField<T>.updateNotNull(update: (current: T) -> T): Boolean {
    return safeLet(value) { v ->
        val newValue = update(v)
        if (newValue != value) {
            value = newValue
            true
        } else false
    } ?: throw NullPointerException("Value is null! Update can not be completed!")
}