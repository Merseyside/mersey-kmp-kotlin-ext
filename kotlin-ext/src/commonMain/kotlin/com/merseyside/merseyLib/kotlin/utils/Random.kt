package com.merseyside.merseyLib.kotlin.utils

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

fun randomString(length: Int): String {
    if (length > 0) {
        val charPool: List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')

        return (1..length)
            .map { Random.nextInt(0, charPool.size) }
            .map(charPool::get)
            .joinToString("")
    }

    return ""
}