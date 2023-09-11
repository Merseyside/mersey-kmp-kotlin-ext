package com.merseyside.merseyLib.kotlin.reflect

expect interface ReflectionHelper {

    val source: Any
}
    /**
     * Sets [value] to a property with name passed in [name]
     */
    expect fun ReflectionHelper.set(name: String, value: Any)

    /**
     * @return an object by [name]
     */
    expect fun <T> ReflectionHelper.get(name: String): T

    /**
     * @return constructor's field names
     */
    expect fun ReflectionHelper.getConstructorFields(): List<String>?

    /**
     * @return all member names
     */
    expect fun ReflectionHelper.getMemberNames(): List<String>