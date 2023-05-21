package com.merseyside.merseyLib.kotlin.observable.ext

import com.merseyside.merseyLib.kotlin.observable.MutableObservableField

/**
 * Updates observable field with providing current value.
 * @return true if current and new values are not equals, false otherwise.
 */
fun <T> MutableObservableField<T>.compareAndUpdateNullable(update: (current: T?) -> T?): Boolean {
    val newValue = update(value)
    return compareAndSetNullable(newValue)
}

@Throws(NullPointerException::class)
fun <T> MutableObservableField<T>.compareAndUpdate(update: (current: T) -> T): Boolean {
    val newValue = update(valueNotNull())
    return compareAndSet(newValue)
}

fun <T> MutableObservableField<T>.updateNullable(update: (current: T?) -> T?) {
    value = update(value)
}

@Throws(NullPointerException::class)
fun <T> MutableObservableField<T>.update(update: (current: T) -> T) {
    value = update(valueNotNull())
}