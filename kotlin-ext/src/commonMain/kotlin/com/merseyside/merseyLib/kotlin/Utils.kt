package com.merseyside.merseyLib.kotlin

import kotlin.math.max
import kotlin.math.min
import kotlin.random.Random

fun randomBool(positiveProbability: Float = 0.5F): Boolean {
    return when {
        positiveProbability >= 1f -> true
        positiveProbability <= 0f -> false

        else -> {
            Random.nextFloat() <= positiveProbability
        }
    }
}

fun generateRandomString(length: Int): String {
    if (length > 0) {
        val charPool: List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')

        return (1..length)
            .map { Random.nextInt(0, charPool.size) }
            .map(charPool::get)
            .joinToString("")
    }

    return ""
}

fun shrinkNumber(number: Number): String {
    val long = number.toLong()

    return when {
        long < 1000 -> {
            long.toString()
        }
        long < 1_000_000 -> {
            "${long / 1000}K+"
        }
        else -> {
            "${long / 1_000_000}M+"
        }
    }
}

fun String.hexStringToByteArray() = ByteArray(this.length / 2) { this.substring(it * 2, it * 2 + 2).toInt(16).toByte() }

fun <T> merge(first: List<T>, second: List<T>): List<T> {
    val list: MutableList<T> = ArrayList(first)
    list.addAll(second)
    return list
}

fun <T: Number> getMinMax(first: T, second: T): Pair<T, T> {
    val min = min(first.toInt(), second.toInt())
    val max = max(first.toInt(), second.toInt())

    return min as T to max as T
}