@file:Suppress("UNCHECKED_CAST")

package com.merseyside.merseyLib.kotlin.reflect

import kotlin.reflect.KFunction
import kotlin.reflect.KMutableProperty
import kotlin.reflect.KProperty
import kotlin.reflect.KProperty1
import kotlin.reflect.full.declaredMemberFunctions
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.primaryConstructor

actual class ReflectWrapper<T : Any> actual constructor(source: T) {
    
    actual val source: T = source

    actual val properties: List<KProperty<*>> by lazy {
        source::class.declaredMemberProperties.toList()
    }

    actual val functions: List<KFunction<*>> by lazy {
        source::class.declaredMemberFunctions.toList()
    }

    actual fun callFunction(name: String, vararg args: Any) {
        val func = functions.find { func -> func.name == name } ?: throw NullPointerException()
        callFunction(func)
    }

    actual fun callFunction(func: KFunction<*>, vararg args: Any) {
        func.call(source, args)
    }

    /**
     * Sets [value] to a property with name passed in [name]
     */
    actual fun <T> setPropertyValue(name: String, value: T) {
        val property: KProperty<T>? = getPropertyByName(name)

        if (property != null) {
            setPropertyValue(property, value)
        } else {
            throw KotlinNullPointerException()
        }
    }

    actual fun <T> setPropertyValue(property: KProperty<T>, value: T) {
        return if (property is KMutableProperty<*>) {
            property.setter.call(source, value)
        } else {
            throw IllegalAccessException("Can't set value to ${property.name} in account of " +
                    "it is not mutable property.")
        }
    }

    /**
     * @return an object by [name]
     */
    actual fun <T> getPropertyValue(name: String): T {
        val property: KProperty<T>? = getPropertyByName(name)
        if (property != null) {
            return getPropertyValue(property)
        } else throw KotlinNullPointerException()
    }

    actual fun <T> getPropertyValue(property: KProperty<T>): T {
        return property.getter.call(source)
    }

    /**
     * @return constructor's field names
     */
    actual fun getConstructorParams(): List<String>? {
        return source::class.primaryConstructor?.parameters
            ?.mapNotNull { it.name }
    }

    /**
     * @return all member names
     */
    private fun <T> getPropertyByName(name: String): KProperty1<*, T>? {
        return properties.find { it.name == name } as? KProperty1<*, T>
    }

    actual fun getPropertyValues(): List<Any> {
        TODO()
        //return properties.map { prop -> getPropertyValue(prop as KProperty<Any>) }
    }
}

