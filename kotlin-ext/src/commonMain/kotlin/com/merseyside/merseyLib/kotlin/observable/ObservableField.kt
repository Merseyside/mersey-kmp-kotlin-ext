package com.merseyside.merseyLib.kotlin.observable

import com.merseyside.merseyLib.kotlin.logger.ILogger

expect abstract class ObservableField<T> constructor(initialValue: T?): ILogger {
    open var value: T?

    protected val observerList: MutableList<(T) -> Unit>

    fun observe(block: (T) -> Unit): Disposable<T>

    fun removeObserver(block: (T) -> Unit): Boolean

    protected fun notifyObservers()

    fun removeAllObservers()
}

expect open class MutableObservableField<T>(initialValue: T? = null) : ObservableField<T>

expect open class SingleObservableField<T>(initialValue: T? = null) : MutableObservableField<T> {

    override var value: T?
}

expect class SingleObservableEvent(): SingleObservableField<Unit> {
    fun call()
}

class Disposable<T>(
    private val field: ObservableField<T>,
    private val observer: (T) -> Unit
) {
    fun dispose() {
        field.removeObserver(observer)
    }
}