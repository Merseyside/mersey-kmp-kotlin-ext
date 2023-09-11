package com.merseyside.merseyLib.kotlin.reflect

actual interface ReflectionHelper {
    actual val source: Any
}

/**
 * Sets [value] to a property with name passed in [name]
 */
actual fun ReflectionHelper.set(name: String, value: Any) {}

/**
 * @return an object by [name]
 */
actual fun <T> ReflectionHelper.get(name: String): T { TODO() }

/**
 * @return constructor's field names
 */
actual fun ReflectionHelper.getConstructorFields(): List<String>? { TODO() }

/**
 * @return all member names
 */
actual fun ReflectionHelper.getMemberNames(): List<String> { TODO() }
