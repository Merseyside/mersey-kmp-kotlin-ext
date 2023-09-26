package com.merseyside.merseyLib.kotlin.observable.ext

import com.merseyside.merseyLib.kotlin.observable.MutableObservableField
import com.merseyside.merseyLib.kotlin.observable.ObservableField
import com.merseyside.merseyLib.kotlin.utils.safeLet

fun <T, R> ObservableField<T>.map(mapper: (T?) -> R?): ObservableField<R?> {
    val initialValue = safeLet(value) { v -> mapper(v) }
    val field = MutableObservableField<R?>(initialValue)

    observeNullable(ignoreCurrent = true) { value ->
        field.value = mapper(value)
    }

    return field
}

fun <T, R> ObservableField<T>.mapNotNull(mapper: (T) -> R): ObservableField<R> {
    val initialValue = safeLet(value) { v -> mapper(v) }
    val field = MutableObservableField(initialValue)

    observe(ignoreCurrent = true) { value ->
        field.value = mapper(value)
    }

    return field
}