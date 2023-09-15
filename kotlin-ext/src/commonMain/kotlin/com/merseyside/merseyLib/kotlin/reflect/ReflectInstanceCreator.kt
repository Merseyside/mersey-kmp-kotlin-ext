package com.merseyside.merseyLib.kotlin.reflect

import kotlin.reflect.KTypeParameter

expect class ReflectInstanceCreator<T : Any> private constructor() {


    companion object {
        inline fun <reified T : Any> initWith(): ReflectInstanceCreator<T>
    }
}