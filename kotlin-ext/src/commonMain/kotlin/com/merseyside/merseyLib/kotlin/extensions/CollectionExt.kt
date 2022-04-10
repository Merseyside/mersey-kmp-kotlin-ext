package com.merseyside.merseyLib.kotlin.extensions

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract
import kotlin.reflect.KClass

@OptIn(ExperimentalContracts::class)
fun Collection<*>?.isNotNullAndEmpty(): Boolean {
    contract {
        returns(true) implies (this@isNotNullAndEmpty != null)
    }

    return this != null && this.isNotEmpty()
}

inline fun <T, R> List<T>?.isNotNullAndEmpty(block: List<T>.() -> R): R? {
    return if (this.isNotNullAndEmpty()) {
        block()
    } else {
        null
    }
}

fun <T> List<T>.removeEqualItems(): List<T> {
    return this.toSet().toList()
}

fun <T> List<T>.unique(
    predicate: (obj1: T, obj2: T) -> Boolean = { obj1, obj2 -> obj1 == obj2 }
): List<T> {
    return if (isNotEmpty()) {
        val uniqueList = ArrayList<T>()

        forEachIndexed { index, value ->
            if (index.isZero()) {
                uniqueList.add(value)
            } else {
                val found = uniqueList.find { predicate.invoke(it, value) }
                if (found == null) uniqueList.add(0, value)
            }
        }

        uniqueList.reversed()
    } else {
        this
    }
}

fun <T : Any> MutableList<T>.remove(predicate: (T) -> Boolean): Boolean {
    forEach {
        if (predicate(it)) {
            return remove(it)
        }
    }

    return false
}

fun <T: Any, R : Comparable<R>> List<T>.minByNullable(selector: (T) -> R?): T? {

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

inline fun <T> List<T?>.forEachNotNull(action: (T) -> Unit) {
    return this.filterNotNull().forEach(action)
}

fun List<Boolean>.forEachIsTrue(): Boolean {
    return this.find { !it } != null
}

fun <T: Any> List<List<T>>.union(): List<T> {
    val hasEmptyList = find { it.isEmpty() } != null

    if (hasEmptyList || isEmpty()) return emptyList()
    if (size == 1) return first()

    var resultList = first().toSet()

    (1 until size).forEach { index ->
        resultList = resultList.union(get(index))
    }

    return resultList.toList()
}

fun <T: Any> List<List<T>>.intersect(): List<T> {
    val hasEmptyList = find { it.isEmpty() } != null

    if (hasEmptyList || isEmpty()) return emptyList()
    if (size == 1) return first()

    var resultList = first().toSet()

    (1 until size).forEach { index ->
        resultList = resultList.intersect(get(index))
    }

    return resultList.toList()
}

inline fun <K, V> Map<out K, V>.forEachEntry(action: (key: K, value: V) -> Unit) {
    forEach { entry -> action(entry.key, entry.value) }
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

inline fun <T, R> Iterable<T>.flatMapNotNull(transform: (T) -> Iterable<R>?): List<R> {
    return flatMap { transform(it) ?: emptyList()  }
}

inline fun <T> Iterable<T>.separate(predicate: (T) -> Boolean): Pair<List<T>, List<T>> {
    val trueList = mutableListOf<T>()
    val falseList = mutableListOf<T>()

    forEach {
        if (predicate(it)) trueList.add(it)
        else falseList.add(it)
    }

    return trueList to falseList
}

fun <T> List<T>.merge(vararg lists: List<T>): List<T> {
    if (lists.isEmpty()) throw IllegalArgumentException("Pass at least one list!")
    val list: MutableList<T> = ArrayList(this)

    lists.forEach {
        list.addAll(it)
    }

    return list
}

inline fun <reified R> Iterable<R>.findIsInstance(): R {
    return filterIsInstance<R>().first()
}

inline fun <reified R> Iterable<R>.findLastIsInstance(): R {
    return filterIsInstance<R>().last()
}