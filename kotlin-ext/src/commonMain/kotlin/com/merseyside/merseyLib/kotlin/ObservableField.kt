package com.merseyside.merseyLib.kotlin

abstract class ObservableField<T> {
    abstract val value: T?

    protected val observerList: MutableList<(T) -> Unit> = mutableListOf()

    fun observe(block: (T) -> Unit): Disposable<T> {
        observerList.add(block)
        value?.let {
            block(it)
        }

        return Disposable(this, block)
    }

    fun removeObserver(block: (T) -> Unit): Boolean {
        return observerList.remove(block)
    }

    protected fun notifyObservers() {
        value?.let {
            if (observerList.isNotEmpty()) {
                observerList.forEach { observer -> observer(it) }
            }
        }
    }

    fun removeAllObservers() {
        observerList.clear()
    }
}

open class MutableObservableField<T>(initialValue: T? = null) : ObservableField<T>() {

    override var value: T? = initialValue
        set(value) {
            field = value

            if (value != null) {
                notifyObservers()
            }
        }
}

class SingleObservableField<T>(initialValue: T? = null) : MutableObservableField<T>() {
    override var value: T? = initialValue
        get() = field.also { value = null }
        set(value) {
            if (value != null) {
                notifyObservers()
            }
        }
}

class Disposable<T>(
    private val field: ObservableField<T>,
    private val observer: (T) -> Unit
) {
    fun dispose() {
        field.removeObserver(observer)
    }
}