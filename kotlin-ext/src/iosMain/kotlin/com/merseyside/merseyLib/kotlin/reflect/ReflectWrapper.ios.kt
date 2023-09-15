package com.merseyside.merseyLib.kotlin.reflect

import kotlin.reflect.KFunction
import kotlin.reflect.KProperty

actual class ReflectWrapper<T : Any> actual constructor(source: T) {

    actual val source: T = source

    actual val properties: List<KProperty<*>> = TODO()

    actual val functions: List<KFunction<*>> = TODO()

    /**
     * Sets [value] to a property with name passed in [name]
     */

    actual fun <T> setPropertyValue(name: String, value: T) { TODO() }

    actual fun <T> setPropertyValue(property: KProperty<T>, value: T) { TODO() }

    /**
     * @return an object by [name]
     */
    actual fun <T> getPropertyValue(name: String): T { TODO() }

    actual fun <T> getPropertyValue(property: KProperty<T>): T { TODO() }

    actual fun callFunction(name: String, vararg args: Any) { TODO() }

    actual fun callFunction(func: KFunction<*>, vararg args: Any) { TODO() }

    /**
     * @return constructor's field names
     */
    actual fun getConstructorParams(): List<String>? { TODO() }
    actual fun getPropertyValues(): List<Any> {
        TODO("Not yet implemented")
    }
}



