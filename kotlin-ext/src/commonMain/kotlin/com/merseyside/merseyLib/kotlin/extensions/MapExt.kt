package com.merseyside.merseyLib.kotlin.extensions

inline fun <K, V> Map<out K, V>.forEachEntry(action: (key: K, value: V) -> Unit) {
    forEach { entry -> action(entry.key, entry.value) }
}