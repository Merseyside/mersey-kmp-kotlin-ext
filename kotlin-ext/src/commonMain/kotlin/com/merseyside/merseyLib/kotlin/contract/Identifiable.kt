package com.merseyside.merseyLib.kotlin.contract

interface Identifiable<Id> {

    fun getId(): Id
}

fun <T> T.areItemsTheSame(other: T): Boolean
    where T : Identifiable<Any> {
    return getId() == other.getId()
}