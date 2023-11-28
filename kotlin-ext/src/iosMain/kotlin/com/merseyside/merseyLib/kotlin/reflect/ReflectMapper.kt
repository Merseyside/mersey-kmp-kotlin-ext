package com.merseyside.merseyLib.kotlin.reflect

actual class ReflectMapper {

    actual companion object {
        actual inline fun <reified T : Any, reified R : Any> map(
            value: T,
            noinline fallback: Fallback
        ): R {
            TODO()
        }
    }

}