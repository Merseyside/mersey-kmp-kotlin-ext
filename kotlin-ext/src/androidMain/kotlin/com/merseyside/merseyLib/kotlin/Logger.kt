package com.merseyside.merseyLib.kotlin

import android.util.Log

actual object Logger {

    actual var isEnabled: Boolean = true
    actual var isDebug = true

    actual fun log(tag: Any?, msg: Any?) {
        if (isLogging()) {
            Log.d(adoptTag(tag), adoptMsg(msg))
        }
    }

    actual fun log(msg: Any?) {
        log(TAG, msg)
    }

    actual fun logErr(tag: Any?, msg: Any?) {
        if (isLogging()) {
            Log.e(adoptTag(tag), adoptMsg(msg))
        }
    }

    actual fun logErr(msg: Any?) {
        logErr("", msg)
    }

    actual fun logErr(tag: Any?, msg: String?, throwable: Throwable) {
        logErr(tag, msg)
        throwable.printStackTrace()
    }

    actual fun logInfo(tag: Any?, msg: Any?) {
        if (isLogging()) {
            Log.i(adoptTag(tag), adoptMsg(msg))
        }
    }

    actual fun logInfo(msg: Any?) {
        logInfo("", msg)
    }

    actual fun logWtf(tag: Any?, msg: Any?) {
        if (isLogging()) {
            Log.wtf(adoptTag(tag), adoptMsg(msg))
        }
    }

    actual fun logWtf(msg: Any?) {
        logWtf("", msg)
    }

    actual fun logErr(throwable: Throwable) {
        if (isLogging()) {
            throwable.printStackTrace()
            Log.d(TAG, throwable.message ?: "No message")
        }
    }

    actual fun logErr(tag: Any?, throwable: Throwable) {
        if (isLogging()) {
            Log.d(adoptTag(tag), throwable.message ?: "No message")
        }
    }

    private fun isLogging(): Boolean {
        return isEnabled && isDebug
    }

    internal actual fun adoptTag(tag: Any?): String {

        return if (tag != null) {
            val strTag = if (tag is String) {
                tag
            } else {
                tag.javaClass.simpleName
            }

            strTag.ifEmpty { TAG }
        } else {
            TAG
        }
    }

    internal actual fun adoptMsg(msg: Any?): String {
        return when (msg) {
            null -> { "null" }

            is String -> { msg }

            is Collection<*> -> {
                if (msg.isEmpty()) {
                    "Empty collection"
                } else {
                    msg.joinToString(separator = "\n")
                }
            }

            is Map<*, *> -> {
                if (msg.isEmpty()) {
                    "Empty map"
                } else {
                    msg.map { "${it.key}: ${it.value}" }
                        .joinToString(separator = "\n")
                }
            }

            is Array<*> -> {
                if (msg.isEmpty()) {
                    "Empty array"
                } else {
                    msg.joinToString(separator = "\n")
                }
            }

            else -> { msg.toString() }
        }
    }

    actual val TAG = "Logger"
}