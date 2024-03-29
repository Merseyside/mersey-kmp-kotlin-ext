package com.merseyside.merseyLib.kotlin.observable

import com.merseyside.merseyLib.kotlin.logger.ILogger

expect abstract class ObservableField<T> constructor(initialValue: T?): ILogger {
    open var value: T?

    protected val nullableObserverList: MutableSet<Observer<T?>>
    protected val observerList: MutableSet<Observer<T>>

    fun observe(ignoreCurrent: Boolean = false, observer: Observer<T>): Disposable<T>

    fun observe(observer: Observer<T>): Disposable<T>

    open fun addObserver(observer: Observer<T>): Disposable<T>

    fun observeNullable(ignoreCurrent: Boolean = false, observer: Observer<T?>): Disposable<T>

    fun removeObserver(observer: Observer<*>): Boolean

    protected fun notifyObservers()

    fun removeAllObservers()

    fun interface Observer<in T> {
        operator fun invoke(value: T)
    }
}

expect open class MutableObservableField<T>(initialValue: T? = null) : ObservableField<T>

expect open class SingleObservableField<T>(initialValue: T? = null) : MutableObservableField<T> {

    override var value: T?
}

expect open class SingleObservableEvent(): SingleObservableField<Unit> {
    fun call()
}

abstract class Disposable<T> {

    abstract fun dispose(): Boolean
}

class SingleDisposable<T>(
    private val field: ObservableField<T>,
    private val observer: ObservableField.Observer<*>
): Disposable<T>() {

    override fun dispose(): Boolean {
        return field.removeObserver(observer)
    }
}

typealias EventObservableField = ObservableField<Unit>