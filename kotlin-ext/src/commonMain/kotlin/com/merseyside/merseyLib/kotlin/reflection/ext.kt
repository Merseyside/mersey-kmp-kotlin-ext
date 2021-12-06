package com.merseyside.merseyLib.kotlin.reflection

import kotlin.reflect.full.memberFunctions

fun Any.callMethodByName(name: String): Any? {
    return this::class.memberFunctions.single { it.name == name }.call(this)
}