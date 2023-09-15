package com.merseyside.merseyLib.kotlin.reflect

import kotlin.reflect.KType

expect class ReflectMapper {

    companion object {
        inline fun <reified T : Any, reified R : Any> map(
            value: T,
            noinline fallback: Fallback = null
        ): R
    }
}

typealias Fallback = ((value: Any, inType: KType, outType: KType) -> Any)?