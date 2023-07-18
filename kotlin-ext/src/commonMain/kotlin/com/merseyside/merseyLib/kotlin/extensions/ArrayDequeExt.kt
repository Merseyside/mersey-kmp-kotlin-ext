package com.merseyside.merseyLib.kotlin.extensions

inline fun <E> ArrayDeque<E>.iteratePop(action: (E) -> Unit) {
    while(iterator().hasNext()) {
        action(removeFirst())
    }
}