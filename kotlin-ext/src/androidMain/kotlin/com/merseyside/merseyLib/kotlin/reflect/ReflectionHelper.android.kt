@file:Suppress("UNCHECKED_CAST")

package com.merseyside.merseyLib.kotlin.reflect

import kotlin.reflect.KCallable
import kotlin.reflect.KMutableProperty
import kotlin.reflect.KProperty
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.primaryConstructor

actual interface ReflectionHelper {
    actual val source: Any
}
/**
 * Sets [value] to a property with name passed in [name]
 */
actual fun ReflectionHelper.set(name: String, value: Any) {
    val property = getPropertyByName(name)

    if (property != null) {
        return if (property is KMutableProperty<*>) {
            property.setter.call(source, value)
        } else {
            throw IllegalAccessException()
        }
    } else {
        throw KotlinNullPointerException()
    }
}

/**
 * @return an object by [name]
 */
actual fun <T> ReflectionHelper.get(name: String): T {
    val property = getPropertyByName(name)

    if (property != null) {
        return if (property is KProperty) {
            property.getter.call(source) as T
        } else {
            throw IllegalAccessException()
        }
    } else {
        throw KotlinNullPointerException()
    }
}

/**
 * @return constructor's field names
 */
actual fun ReflectionHelper.getConstructorFields(): List<String>? {
    return source::class.primaryConstructor?.parameters
        ?.mapNotNull { it.name }
}

/**
 * @return all member names
 */
actual fun ReflectionHelper.getMemberNames(): List<String> {
    return this::class.declaredMemberProperties.map { it.name }
}

private fun ReflectionHelper.getPropertyByName(name: String): KCallable<*>? {
    return source::class.members.find { it.name == name }
}