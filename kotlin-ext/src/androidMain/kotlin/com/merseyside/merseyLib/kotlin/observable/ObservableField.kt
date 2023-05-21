package com.merseyside.merseyLib.kotlin.observable

import androidx.databinding.Bindable
import androidx.databinding.Observable
import com.merseyside.merseyLib.kotlin.BR
import com.merseyside.merseyLib.kotlin.extensions.isNotZero
import com.merseyside.merseyLib.kotlin.logger.ILogger
import androidx.databinding.BaseObservable as AndroidObservable

actual abstract class ObservableField<T> actual constructor(
    initialValue: T?
) : AndroidObservable(), ILogger {
    @Bindable
    actual open var value: T? = initialValue
        internal set(value) {
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

    protected actual val observerList: MutableList<(T) -> Unit> = mutableListOf()

    actual fun observe(ignoreCurrent: Boolean, observer: (T) -> Unit): Disposable<T> {
        observerList.add(observer)
        if (!ignoreCurrent) {
            value?.let {
                observer(it)
            }
        }

        return Disposable(this, observer)
    }

    actual fun observeNullable(ignoreCurrent: Boolean, observer: (T?) -> Unit): Disposable<T> {
        observerList.add(observer)
        if (!ignoreCurrent) {
            observer(value)
        }

        return Disposable(this, observer)
    }

    actual fun removeObserver(block: (T) -> Unit): Boolean {
        return observerList.remove(block)
    }

    protected actual fun notifyObservers() {
        value?.let {
            if (observerList.isNotEmpty()) {
                observerList.forEach { observer -> observer(it) }
            }
        }
    }

    actual fun removeAllObservers() {
        observerList.clear()
    }

    override val tag: String = "ObservableField"

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
        public set(v) {
            super.value = v
        }
}

actual open class SingleObservableField<T> actual constructor(initialValue: T?) :
    MutableObservableField<T>(initialValue) {
    @get:Bindable
    actual override var value: T?
        get() = super.value.also {
            if (it != null) value = null }
        set(v) {
            super.value = v
        }
}

actual class SingleObservableEvent : SingleObservableField<Unit>(null) {
    actual fun call() {
        value = Unit
    }
}