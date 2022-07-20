package com.merseyside.merseyLib.kotlin.logger

import com.merseyside.merseyLib.kotlin.logger.log

interface ILogger {
    val tag: String

    fun log(msg: String) {
        Logger.log(tag, msg)
    }

    fun <T> T?.log(prefix: Any? = null, suffix: Any? = null) {
        log(tag, prefix, suffix) { this }
    }
}