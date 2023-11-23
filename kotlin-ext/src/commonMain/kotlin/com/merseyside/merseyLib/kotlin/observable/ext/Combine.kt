package com.merseyside.merseyLib.kotlin.observable.ext

import com.merseyside.merseyLib.kotlin.observable.MutableObservableField
import com.merseyside.merseyLib.kotlin.observable.ObservableField

@Suppress("UNCHECKED_CAST")
private fun <T, R> combine(
    vararg fields: ObservableField<*>,
    transform: (List<T>) -> R
): ObservableField<R> {

    fun getAllValues(): List<Any?> {
        return fields.map { it.value }
    }

    val mutableObservableField = MutableObservableField<R>()

    val triggerObserver = ObservableField.Observer<T> {
        mutableObservableField.value = transform(getAllValues() as List<T>)
    }

    fields.forEach { field ->
        (field as ObservableField<T>).observe(ignoreCurrent = false, observer = triggerObserver)
    }

    return mutableObservableField

}

@Suppress("UNCHECKED_CAST")
fun <T1, T2, R> combineFields(
    of1: ObservableField<T1>,
    of2: ObservableField<T2>,
    transform: (T1, T2) -> R
): ObservableField<R> {
    return combine(of1, of2) { args: List<Any> ->
        transform(
            args[0] as T1,
            args[1] as T2
        )
    }
}