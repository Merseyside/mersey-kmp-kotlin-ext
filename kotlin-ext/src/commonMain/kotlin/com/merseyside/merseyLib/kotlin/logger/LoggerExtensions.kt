package com.merseyside.merseyLib.kotlin.logger

import com.merseyside.merseyLib.kotlin.logger.Logger.log as loggerLog

inline fun <reified T> T.log(tag: Any = T::class.simpleName ?: Logger.TAG, prefix: Any? = null, suffix: Any? = null): T {
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

fun <T> T?.log(
    tag: Any = Logger.TAG,
    prefix: Any? = null,
    suffix: Any? = null,
    msgBlock: T?.() -> T?
): T? {
    val msg = "${prefix ?: ""} ${Logger.adoptMsg(msgBlock(this))} ${suffix ?: ""}"
    Logger.log(tag, msg)

    return this
}