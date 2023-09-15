package com.merseyside.merseyLib.kotlin.extensions

inline fun <T> Iterable<T?>.forEachNotNull(action: (T) -> Unit) {
    return this.filterNotNull().forEach(action)
}

fun Iterable<Boolean>.forEachIsTrue(): Boolean {
    return this.find { !it } != null
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

fun <T1, T2> Iterable<T1>.subtractBy(
    other: Iterable<T2>,
    predicate: (first: T1, second: T2) -> Boolean
): Set<T1> {
    if (other.count().isZero()) return toSet()
    return filter { first ->
        other.find { second -> predicate(first, second) } == null
    }.toSet()
}

fun <T1, T2> Iterable<T1>.intersectBy(
    other: Iterable<T2>,
    predicate: (first: T1, second: T2) -> Boolean
): Set<T1> {
    if (count().isZero() || other.count().isZero()) return emptySet()
    return filter { first ->
        other.find { second -> predicate(first, second) } != null
    }.toSet()
}

inline fun <reified R> Iterable<R>.findIsInstance(): R {
    return filterIsInstance<R>().first()
}

inline fun <reified R> Iterable<R>.findLastIsInstance(): R {
    return filterIsInstance<R>().last()
}