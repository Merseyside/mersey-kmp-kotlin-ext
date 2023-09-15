package com.merseyside.merseyLib.kotlin.reflect

import kotlin.reflect.KFunction
import kotlin.reflect.KProperty

expect class ReflectWrapper<T : Any> constructor(source: T) {

    val source: T

    val properties: List<KProperty<*>>

    val functions: List<KFunction<*>>

    /**
     * Sets [value] to a property with name passed in [name]
     */

    fun <T> setPropertyValue(name: String, value: T)

    fun <T> setPropertyValue(property: KProperty<T>, value: T)

    /**
     * @return an object by [name]
     */
    fun <T> getPropertyValue(name: String): T

    fun <T> getPropertyValue(property: KProperty<T>): T

    fun getPropertyValues(): List<Any>

    fun callFunction(name: String, vararg args: Any)

    fun callFunction(func: KFunction<*>, vararg args: Any)

    /**
     * @return constructor's field names
     */
    fun getConstructorParams(): List<String>?
}

/**
 * @return all member names
 */
fun ReflectWrapper<*>.getPropertyNames(): List<String> {
    return properties.map { prop -> prop.name }
}

fun ReflectWrapper<*>.getFunctionNames(): List<String> {
    return functions.map { prop -> prop.name }
}

fun ReflectWrapper<*>.hasProperty(name: String): Boolean {
    return getPropertyNames().contains(name)
}

fun ReflectWrapper<*>.hasFunction(name: String): Boolean {
    return getFunctionNames().contains(name)
}
