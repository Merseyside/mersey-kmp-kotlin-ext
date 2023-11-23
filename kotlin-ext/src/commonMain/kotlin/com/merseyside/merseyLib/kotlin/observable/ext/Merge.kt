package com.merseyside.merseyLib.kotlin.observable.ext

import com.merseyside.merseyLib.kotlin.observable.*

fun mergeSingleEvent(
    observableFieldList: List<ObservableField<Unit>>
): EventObservableField = object : SingleObservableEvent() {

    private val mergingFields: List<ObservableField<Unit>> = observableFieldList

    private val observer = Observer<Unit> {
        call()
    }

    override fun addObserver(observer: Observer<Unit>): Disposable<Unit> {
        val fieldDisposable = super.addObserver(observer)
        val disposables = mergingFields.map { field ->
            field.observe(this.observer)
        }

        return object : Disposable<Unit>() {

            private var disposables: List<Disposable<Unit>> = disposables

            override fun dispose(): Boolean {
                this.disposables.forEach { disposable -> disposable.dispose() }
                this.disposables = emptyList()

                return fieldDisposable.dispose()
            }
        }
    }

    override val tag: String
        get() = "MergedSingleEvent"
}

fun <T> merge(
    observableFieldList: List<ObservableField<T>>,
): ObservableField<T> {
    val mutField = MutableObservableField<T>()
    val observer = ObservableField.Observer<T> { value ->
        mutField.value = value
    }

    observableFieldList.forEach { field ->
        field.observe(observer)
    }
    return mutField
}

fun <T> merge(
    vararg observableFields: ObservableField<T>
): ObservableField<T> {
    return merge(observableFieldList = observableFields.toList())
}