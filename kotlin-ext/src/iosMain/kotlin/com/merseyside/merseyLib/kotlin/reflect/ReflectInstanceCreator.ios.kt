package com.merseyside.merseyLib.kotlin.reflect

actual class ReflectInstanceCreator<T : Any> private actual constructor() {

    actual companion object {
        actual inline fun <reified T : Any> initWith(): ReflectInstanceCreator<T> {
            TODO()
        }
    }
}