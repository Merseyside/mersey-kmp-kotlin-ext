package com.merseyside.merseyLib.kotlin.logger

interface ILogger {
    val tag: String

    fun logMsg(msg: String) {
        Logger.log(tag, msg)
    }

    fun <T> T.log(prefix: Any? = null, suffix: Any? = null): T {
        return logSimpleTag(tag, prefix, suffix)
    }
}