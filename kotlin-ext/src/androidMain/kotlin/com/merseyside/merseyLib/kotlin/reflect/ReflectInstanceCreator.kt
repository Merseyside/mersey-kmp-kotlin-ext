package com.merseyside.merseyLib.kotlin.reflect

import kotlin.reflect.KClass
import kotlin.reflect.KParameter
import kotlin.reflect.full.primaryConstructor

actual class ReflectInstanceCreator<T : Any> private actual constructor() {

    constructor(clazz: KClass<T>) : this() {
        this.clazz = clazz
    }

    private lateinit var clazz: KClass<T>
    private val primaryConstructor by lazy {
        clazz.primaryConstructor ?: throw NullPointerException()
    }

    fun getPrimaryConstructorParams(): List<KParameter> {
        return primaryConstructor.parameters
    }

    fun createInstance(vararg args: Any?): T {
        return primaryConstructor.call(*args)
    }

    actual companion object {
        actual inline fun <reified T : Any> initWith(): ReflectInstanceCreator<T> {
            return ReflectInstanceCreator(T::class)
        }
    }
}