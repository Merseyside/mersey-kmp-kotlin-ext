package com.merseyside.merseyLib.kotlin.observable.ext

import com.merseyside.merseyLib.kotlin.observable.*

fun mergeSingleEvent(
    observableFieldList: List<ObservableField<Unit>>
): EventObservableField {
    val mutField = SingleObservableEvent()

    val observer = ObservableField.Observer<Unit> {
        mutField.call()
    }

    observableFieldList.forEach { field ->
        field.observe(observer)
    }

    return mutField
}

fun <T> merge(
    observableFieldList: List<ObservableField<T>>,
): ObservableField<T> {
    val mutField = MutableObservableField<T>()
    val observer = ObservableField.Observer<T> { value ->
        mutField.value = value
    }

    observableFieldList.forEach { field ->
        field.observe(observer) }
    return mutField
}

fun <T> merge(
    vararg observableFields: ObservableField<T>
): ObservableField<T> {
    return merge(observableFieldList = observableFields.toList())
}