package com.merseyside.merseyLib.kotlin.extensions

import com.merseyside.merseyLib.kotlin.Logger
import com.merseyside.merseyLib.kotlin.Logger.log as loggerLog

fun <T> T.log(tag: Any = Logger.TAG, prefix: Any? = null, suffix: Any? = null): T {
    val msg = "${prefix ?: ""} ${Logger.adoptMsg(this)} ${suffix ?: ""}"
    Logger.log(tag, msg)

    return this
}

inline fun <reified T> T.logMsg(
    tag: String = T::class.simpleName ?: Logger.TAG,
    message: Any? = null
): T {
    return this.also { loggerLog(tag, message) }
}

fun <T> T.log(
    tag: Any = Logger.TAG,
    prefix: Any? = null,
    suffix: Any? = null,
    msgBlock: T.() -> Any
): T {
    val msg = "${prefix ?: ""} ${Logger.adoptMsg(msgBlock())} ${suffix ?: ""}"
    Logger.log(tag, msg)

    return this
}