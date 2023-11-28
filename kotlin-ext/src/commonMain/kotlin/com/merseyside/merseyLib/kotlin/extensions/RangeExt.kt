package com.merseyside.merseyLib.kotlin.extensions

fun Int.toRange(offset: Int): IntRange {
    return this - offset .. this + offset
}

fun Int.toRange(offset: Int, s: Int): IntProgression {
    return this - offset .. this + offset step s
}

fun Float.toRange(offset: Float): ClosedRange<Float> {
    return this - offset .. this + offset
}


fun <T : Comparable<T>> ClosedRange<T>.intersect(other: ClosedRange<T>): ClosedRange<T>? {
    val intersectedStart: T =
        if (other.contains(start)) start
        else if (contains(other.start)) other.start
        else return null

    val inclusiveIntersectedEnd: T =
        if (other.contains(endInclusive)) endInclusive
        else other.endInclusive

    return intersectedStart..inclusiveIntersectedEnd
}

fun ClosedRange<Int>.toIntRange(): IntRange {
    return IntRange(start, endInclusive)
}

//@OptIn(ExperimentalStdlibApi::class)
//fun <T : Comparable<T>> OpenEndRange<T>.intersect(other: OpenEndRange<T>): OpenEndRange<T>? {
//    val intersectedStart: T =
//        if (other.contains(start)) start
//        else if (contains(other.start)) other.start
//        else return null
//
//    val inclusiveIntersectedEnd: T =
//        if (other.contains(endExclusive)) endExclusive
//        else other.endExclusive
//
//    return (intersectedStart until inclusiveIntersectedEnd)
//}