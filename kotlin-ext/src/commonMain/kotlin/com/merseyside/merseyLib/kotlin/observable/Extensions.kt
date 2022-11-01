@file:Suppress("UNCHECKED_CAST")

package com.merseyside.merseyLib.kotlin.observable

private fun <R> combine(
    vararg fields: ObservableField<*>,
    transform: (List<*>) -> R
): ObservableField<R> {

    fun getAllValues(): List<*> {
        return fields.map { it.value }
    }

    val mutableObservableField = MutableObservableField<R>()

    val triggerObserver: (Any?) -> Unit = {
        val result = transform(getAllValues())
        mutableObservableField.value = result
    }

    fields.forEach { field ->
        field.observe(triggerObserver)
    }

    return mutableObservableField

}

fun <T1, T2, R> combineFields(
    of1: ObservableField<T1>,
    of2: ObservableField<T2>,
    transform: (T1, T2) -> R
): ObservableField<R> {
    return combine(of1, of2) { args: List<*> ->
        transform(
            args[0] as T1,
            args[1] as T2
        )
    }
}