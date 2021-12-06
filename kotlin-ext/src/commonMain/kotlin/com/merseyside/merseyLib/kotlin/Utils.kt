package com.merseyside.merseyLib.kotlin

import java.util.*

fun randomBool(positiveProbability: Float = 0.5F): Boolean {
    return when {
        positiveProbability >= 1f -> true
        positiveProbability <= 0f -> false

        else -> {
            val rand = Random()
            rand.nextFloat() <= positiveProbability
        }
    }
}

fun generateRandomString(length: Int): String {
    if (length > 0) {
        val charPool: List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')

        return (1..length)
            .map { kotlin.random.Random.nextInt(0, charPool.size) }
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