package com.merseyside.merseyLib.kotlin.reflect

class ReflectWrapper<T : Any>(value: T): ReflectionHelper {

    override val source: Any = value

    val memberNames: List<String> by lazy { getMemberNames() }

    fun contains(memberName: String): Boolean {
        return memberNames.contains(memberName)
    }

}