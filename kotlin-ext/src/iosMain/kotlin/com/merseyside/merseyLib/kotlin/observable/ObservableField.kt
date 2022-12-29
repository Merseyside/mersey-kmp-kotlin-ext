package com.merseyside.merseyLib.kotlin.observable

import com.merseyside.merseyLib.kotlin.logger.ILogger

actual abstract class ObservableField<T> actual constructor(initialValue: T?) : ILogger {
    actual open var value: T? = initialValue
        internal set(value) {
            if (field != value) {
                field = value
                notifyObservers()
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

actual class SingleObservableEvent : SingleObservableField<Unit>(null) {
    actual fun call() {
        value = Unit
    }
}