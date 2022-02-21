package com.merseyside.merseyLib.kotlin

import kotlin.native.concurrent.ThreadLocal

@ThreadLocal
expect object Logger {

    var isEnabled: Boolean
    var isDebug: Boolean

    fun log(tag: Any?, msg: Any? = "Empty msg")
    fun log(msg: Any? = "Empty msg")
    fun logErr(tag: Any?, msg: Any? = "Empty error")
    fun logErr(msg: Any? = "Empty error")
    fun logErr(tag: Any?, throwable: Throwable)
    fun logErr(tag: Any?, msg: String?, throwable: Throwable)
    fun logErr(throwable: Throwable)
    fun logInfo(tag: Any?, msg: Any?)
    fun logInfo(msg: Any?)
    fun logWtf(tag: Any?, msg: Any? = "wtf?")
    fun logWtf(msg: Any? = "wtf?")

    internal fun adoptTag(tag: Any?): String
    internal fun adoptMsg(msg: Any?): String

    val TAG: String
}