package com.merseyside.merseyLib.kotlin.observable

import com.merseyside.merseyLib.kotlin.logger.ILogger

actual abstract class ObservableField<T> actual constructor(initialValue: T?) : ILogger {
    actual open var value: T? = initialValue
        set(value) {
            field = value
            notifyObservers()
        }

    protected actual val nullableObserverList: MutableSet<Observer<T?>> = mutableSetOf()
    protected actual val observerList: MutableSet<Observer<T>> = mutableSetOf()

    actual open fun observe(
        ignoreCurrent: Boolean,
        observer: Observer<T>
    ): Disposable<T> {

        if (!ignoreCurrent) {
            value?.let { observer(it) }
        }
        return addObserver(observer)
    }

    actual open fun observe(observer: Observer<T>): Disposable<T> {
        return observe(ignoreCurrent = false, observer)
    }

    actual open fun addObserver(observer: Observer<T>): Disposable<T> {
        observerList.add(observer)
        return SingleDisposable(this, observer)
    }

    actual open fun observeNullable(
        ignoreCurrent: Boolean,
        observer: Observer<T?>
    ): Disposable<T> {
        nullableObserverList.add(observer)
        if (!ignoreCurrent) {
            observer(value)
        }
        return SingleDisposable(this, observer)
    }

    actual fun removeObserver(observer: Observer<*>): Boolean {
        return nullableObserverList.remove(observer) || observerList.remove(observer)
    }

    protected actual fun notifyObservers() {
        if (nullableObserverList.isNotEmpty()) {
            nullableObserverList.forEach { observer -> observer(value) }
        }

        value?.let {
            if (observerList.isNotEmpty()) {
                observerList.forEach { observer -> observer(it) }
            }
        }
    }

    actual fun removeAllObservers() {
        nullableObserverList.clear()
        observerList.clear()
    }

    actual fun interface Observer<in T> {
        actual operator fun invoke(value: T)
    }

    override val tag: String = "ObservableField"
}

actual open class MutableObservableField<T> actual constructor(initialValue: T?) :
    ObservableField<T>(initialValue) {
    override var value: T?
        get() = super.value
        public set(v) {
            super.value = v
        }
}

actual open class SingleObservableField<T> actual constructor(initialValue: T?) :
    MutableObservableField<T>(initialValue) {
    actual override var value: T? = initialValue
        set(v) {
            super.value = v
        }
        get() = field.also { value = null }
}

actual open class SingleObservableEvent : SingleObservableField<Unit>(null) {
    actual fun call() {
        value = Unit
    }
}