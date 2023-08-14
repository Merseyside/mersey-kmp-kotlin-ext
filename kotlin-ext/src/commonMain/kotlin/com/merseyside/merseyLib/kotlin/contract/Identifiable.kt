package com.merseyside.merseyLib.kotlin.contract

interface Identifiable<Id> {
    val id: Id
}

fun <T> T.areItemsTheSame(other: T): Boolean
    where T : Identifiable<out Any> {
    return id == other.id
}