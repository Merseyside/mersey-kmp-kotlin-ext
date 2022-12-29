package com.merseyside.merseyLib.kotlin.extensions

fun <T> MutableList<T>.addOrSet(position: Int, element: T) {
    if (position > lastIndex) {
        add(position, element)
    } else {
        set(position, element)
    }
}

/**
 * Moves item from old position to new position
 * @return moved item
 */
fun <T> MutableList<T>.move(oldPosition: Int, newPosition: Int): T {
    val item = get(oldPosition)
    removeAt(oldPosition)
    add(newPosition, item)
    return item
}

fun <T> MutableList<T>.move(item: T, newPosition: Int): T {
    val oldPosition = indexOf(item)
    removeAt(oldPosition)
    add(newPosition, item)
    return item
}