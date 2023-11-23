package com.merseyside.merseyLib.kotlin.extensions

private fun <T> List<T>.indexOffset(fromIndex: Int, offset: Int): Int {
    val offsetIndex = fromIndex + offset
    if (offsetIndex > lastIndex) throw IndexOutOfBoundsException()
    return offsetIndex
}

private fun <T> List<T>.backwardIndexOffset(toIndex: Int, offset: Int): Int {
    val offsetIndex = toIndex - offset
    if (offsetIndex < 0) throw IndexOutOfBoundsException()
    return offsetIndex
}

private fun <T> List<T>.indexOffsetSafe(fromIndex: Int, offset: Int): Int {
    return try {
        indexOffset(fromIndex, offset)
    } catch (e: IndexOutOfBoundsException) { size }
}

private fun <T> List<T>.backwardIndexOffsetSafe(toIndex: Int, offset: Int): Int {
    return try {
        backwardIndexOffset(toIndex, offset)
    } catch (e: IndexOutOfBoundsException) { 0 }
}

/**
 * @param anchorIndex always inclusive
 */
fun <T> List<T>.subListSafe(anchorIndex: Int, count: Int? = null, backwardCount: Int? = null): List<T> {
    check(anchorIndex >= 0) {
        "anchorIndex can not be negative!"
    }

    check(anchorIndex < size) {
        "anchorIndex equals $anchorIndex but size is $size"
    }

    check(count != null || backwardCount != null) {
        "Count or/and backwardCount weren't passed!"
    }

    val mutSet = mutableSetOf<T>()

    if (backwardCount != null) {
        if (backwardCount == 0) return emptyList()

        val index = backwardIndexOffsetSafe(toIndex = anchorIndex, offset = backwardCount - 1)
        mutSet.addAll(subList(index, anchorIndex + 1))
    }

    if (count != null) {
        if (count == 0) return emptyList()

        val index = indexOffsetSafe(fromIndex = anchorIndex, offset = count)
        mutSet.addAll(subList(anchorIndex, index))
    }

    return mutSet.toList()
}

fun <T> List<T>.subListSafe(anchorIndex: Int, offset: Int): List<T> {
    return subListSafe(anchorIndex, offset, offset)
}