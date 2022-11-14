package com.merseyside.merseyLib.kotlin.observable

import androidx.databinding.Observable
import com.merseyside.merseyLib.kotlin.extensions.isNotZero
import com.merseyside.merseyLib.kotlin.logger.ILogger
import androidx.databinding.BaseObservable as AndroidObservable

actual open class ObservableField<T> actual constructor() : AndroidObservable(), ILogger {
    actual open var value: T? = null

    constructor(vararg dependencies: Observable): this() {
        if (dependencies.size.isNotZero()) {
            val callback = DependencyCallback()

            dependencies.forEach {
                it.addOnPropertyChangedCallback(callback)
            }
        }
    }

    protected actual val observerList: MutableList<(T) -> Unit> = mutableListOf()

    actual fun observe(block: (T) -> Unit): Disposable<T> {

        observerList.add(block)
        value?.let {
            block(it)
        }

        return Disposable(this, block)
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
    ObservableField<T>() {

    actual override var value: T? = initialValue
        set(value) {

            if (field != value) {
                field = value
                if (value != null) {
                    notifyObservers()
                }
            }
        }
}

actual open class SingleObservableField<T> actual constructor(initialValue: T?) :
    MutableObservableField<T>(initialValue) {
    actual override var value: T? = initialValue
        get() = field.also { value = null }
        set(value) {
            field = value
            if (value != null) {
                notifyObservers()
            }
        }
}

actual class SingleObservableEvent : SingleObservableField<Unit>(null) {
    actual fun call() {
        value = Unit
    }
}