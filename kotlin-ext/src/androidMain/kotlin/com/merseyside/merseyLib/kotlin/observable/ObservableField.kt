package com.merseyside.merseyLib.kotlin.observable

import androidx.annotation.CallSuper
import androidx.databinding.Bindable
import androidx.databinding.Observable
import com.merseyside.merseyLib.kotlin.BR
import com.merseyside.merseyLib.kotlin.extensions.isNotZero
import com.merseyside.merseyLib.kotlin.logger.ILogger
import java.util.Collections
import androidx.databinding.BaseObservable as AndroidObservable

actual abstract class ObservableField<T> actual constructor(initialValue: T?) :
    AndroidObservable(), ILogger {

    @Bindable
    actual open var value: T? = initialValue
        set(value) {
            field = value
            notifyPropertyChanged(BR.value)
            notifyObservers()
        }

    constructor(vararg dependencies: Observable) : this(null) {
        if (dependencies.size.isNotZero()) {
            val callback = DependencyCallback()

            dependencies.forEach {
                it.addOnPropertyChangedCallback(callback)
            }
        }
    }

    protected actual val nullableObserverList: MutableSet<Observer<T?>> = Collections.synchronizedSet(mutableSetOf())
    protected actual val observerList: MutableSet<Observer<T>> = Collections.synchronizedSet(mutableSetOf())

    actual open fun observe(
        ignoreCurrent: Boolean,
        observer: Observer<T>
    ): Disposable<T> {
        if (!ignoreCurrent) {
            value?.let { observer(it) }
        }
        return addObserver(observer)
    }

    actual fun observe(observer: Observer<T>): Disposable<T> {
        return observe(ignoreCurrent = false, observer)
    }

    @CallSuper
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
            nullableObserverList.toList().forEach { observer -> observer(value) }
        }

        value?.let {
            if (observerList.isNotEmpty()) {
                observerList.toList().forEach { observer -> observer(it) }
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

    override val tag: String
        get() = this.toString()

    inner class DependencyCallback : Observable.OnPropertyChangedCallback() {
        override fun onPropertyChanged(sender: Observable, propertyId: Int) {
            notifyChange()
        }
    }
}

actual open class MutableObservableField<T> actual constructor(initialValue: T?) :
    ObservableField<T>(initialValue) {

    @get:Bindable
    override var value: T?
        get() = super.value
        set(v) {
            super.value = v
        }

}

actual open class SingleObservableField<T> actual constructor(initialValue: T?) :
    MutableObservableField<T>(initialValue) {
    @get:Bindable
    actual override var value: T?
        get() = super.value.also {
            if (it != null) value = null
        }
        set(v) {
            super.value = v
        }
}

actual open class SingleObservableEvent : SingleObservableField<Unit>(null) {
    actual fun call() {
        value = Unit
    }
}