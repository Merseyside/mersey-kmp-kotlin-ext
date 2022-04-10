package com.merseyside.merseyLib.kotlin.extensions

import platform.Foundation.NSNumber
import platform.Foundation.NSNumberFormatter

actual fun Double.toFormattedString(fractionDigits: Int): String {
    return NSNumberFormatter().apply {
        minimumFractionDigits = 0u
        maximumFractionDigits = fractionDigits.toULong()
        numberStyle = 1u
    }.stringFromNumber(NSNumber(this)) ?: throw Exception("Can not parse double!")
}