package com.merseyside.merseyLib.kotlin.extensions

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

@OptIn(ExperimentalContracts::class)
fun Collection<*>?.isNotNullAndEmpty(): Boolean {
    contract {
        returns(true) implies (this@isNotNullAndEmpty != null)
    }

    return this != null && this.isNotEmpty()
}

inline fun <M : Collection<T>, T, R> M?.isNotNullAndEmpty(block: M.() -> R): R? {
    return if (this.isNotNullAndEmpty()) {
        block()
    } else {
        null
    }
}

fun <T> Collection<T>.unique(
    predicate: (obj1: T, obj2: T) -> Boolean = { obj1, obj2 -> obj1 == obj2 }
): Set<T> {
    return if (isNotEmpty()) {
        val uniqueList: MutableSet<T> = mutableSetOf()

        forEachIndexed { index, value ->
            if (index.isZero()) {
                uniqueList.add(value)
            } else {
                val found = uniqueList.find { predicate.invoke(it, value) }
                if (found == null) uniqueList.add(value)
            }
        }

        uniqueList.reversed().toSet()
    } else {
        this.toSet()
    }
}

fun <T : Any> MutableCollection<T>.remove(predicate: (T) -> Boolean): Boolean {
    forEach {
        if (predicate(it)) {
            return remove(it)
        }
    }

    return false
}

fun <T, R : Comparable<R>> Collection<T>.minByNullable(selector: (T) -> R?): T? {

    var minValue: R? = null
    var minElement: T? = null

    forEach { value ->
        val selectorValue = selector(value)

        if (selectorValue != null) {
            if (minElement == null) {
                minElement = value
                minValue = selectorValue
            } else {
                if (minValue!!.compareTo(selectorValue) == 1) {
                    minElement = value
                    minValue = selectorValue
                }
            }
        }
    }

    return minElement
}

fun <T> Collection<Collection<T>>.union(): Set<T> {
    val hasEmptyList = find { it.isEmpty() } != null

    if (hasEmptyList || isEmpty()) return emptySet()
    if (size == 1) return first().toSet()

    var resultList: Set<T> = setOf()

    forEach {
        resultList = resultList.union(it)
    }

    return resultList
}

fun <T> Collection<Collection<T>>.intersect(): Set<T> {
    val hasEmptyList = find { it.isEmpty() } != null

    if (hasEmptyList || isEmpty()) return emptySet()
    if (size == 1) return first().toSet()

    var resultList = first().toSet()

    (1 until size).forEach { index ->
        resultList = resultList.intersect(elementAt(index).toSet())
    }

    return resultList
}

inline fun <T, R> Collection<T?>.whenAllNotNull(block: (List<T>) -> R) {
    if (this.all { it != null }) {
        block(this.filterNotNull())
    }
}

inline fun <T, R> Collection<T?>.whenAnyNotNull(block: (List<T>) -> R) {
    if (this.any { it != null }) {
        block(this.filterNotNull())
    }
}

fun <T> Collection<T>.merge(vararg lists: Collection<T>): List<T> {
    if (lists.isEmpty()) throw IllegalArgumentException("Pass at least one list!")
    val list: MutableList<T> = ArrayList(this)

    lists.forEach {
        list.addAll(it)
    }

    return list
}

@Throws(NullPointerException::class)
fun <T> Collection<T>.firstNotNull(): T {
    return find { it != null } ?: throw NullPointerException("No non-null items found!")
}

fun <T> Collection<T>.isEqualsIgnoreOrder(other: Collection<T>): Boolean {
    return this.size == other.size && this.toSet() == other.toSet()
}

fun Collection<Boolean>.flatWithOr(): Boolean {
    return contains(true)
}

fun Collection<Boolean>.flatWithAnd(): Boolean {
    return !contains(false)
}

inline fun <T, R> Iterable<T>.mapWith(transform: T.() -> R): List<R> {
    return map(transform)
}