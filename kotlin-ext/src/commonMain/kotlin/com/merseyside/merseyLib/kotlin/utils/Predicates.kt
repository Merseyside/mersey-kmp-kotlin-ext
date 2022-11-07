package com.merseyside.merseyLib.kotlin.utils

import com.merseyside.merseyLib.kotlin.extensions.firstNotNull

inline fun <T: Any, R: Any> safeLet(p1: T?, block: (T) -> R?): R? {
    return if (p1 != null) block(p1) else null
}

inline fun <T1: Any, T2: Any, R: Any> safeLet(p1: T1?, p2: T2?, block: (T1, T2) -> R?): R? {
    return if (p1 != null && p2 != null) block(p1, p2) else null
}

inline fun <T1: Any, T2: Any, T3: Any, R: Any> safeLet(p1: T1?, p2: T2?, p3: T3?, block: (T1, T2, T3) -> R?): R? {
    return if (p1 != null && p2 != null && p3 != null) block(p1, p2, p3) else null
}

fun <T> firstNotNull(vararg data: T): T {
    return data.toList().firstNotNull()
}

fun <R> Boolean.ifTrue(block: () -> R): R? {
    return if (this) block()
    else null
}

fun <R> Boolean.ifFalse(block: () -> R): R? {
    return if (!this) block()
    else null
}