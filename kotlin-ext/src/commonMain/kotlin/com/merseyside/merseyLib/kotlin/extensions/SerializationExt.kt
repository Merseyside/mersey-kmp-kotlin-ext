package com.merseyside.merseyLib.kotlin.extensions

import com.merseyside.merseyLib.kotlin.serialization.JsonConfigurator
import com.merseyside.merseyLib.kotlin.serialization.deserialize
import com.merseyside.merseyLib.kotlin.serialization.serialize
import kotlinx.serialization.json.*
import kotlin.jvm.JvmName

@JvmName("toStringJsonArray")
fun Collection<String?>.toJsonArray(): JsonArray {
    return buildJsonArray {
        forEach {
            add(it)
        }
    }
}

@JvmName("toIntJsonArray")
fun Collection<Int?>.toJsonArray(): JsonArray {
    return buildJsonArray {
        forEach {
            add(it)
        }
    }
}

@JvmName("toFloatJsonArray")
fun Collection<Float?>.toJsonArray(): JsonArray {
    return buildJsonArray {
        forEach {
            add(it)
        }
    }
}

@JvmName("toBooleanJsonArray")
fun Collection<Boolean?>.toJsonArray(): JsonArray {
    return buildJsonArray {
        forEach {
            add(it)
        }
    }
}

@JvmName("toLongJsonArray")
fun Collection<Long>.toJsonArray(): JsonArray {
    return buildJsonArray {
        forEach {
            add(it)
        }
    }
}

@JvmName("toAnyJsonArray")
inline fun <reified T : Any> Collection<T?>.toJsonArray(json: Json = JsonConfigurator.json): JsonArray {
    return buildJsonArray {
        forEach { item ->
            item?.toJsonObject(json)?.let {
                add(it)
            } ?: run {
                val bool: Boolean? = null
                add(bool)
            }
        }
    }
}

inline fun <reified T : Any> T.toJsonObject(json: Json = JsonConfigurator.json): JsonObject {
    val string = serialize(json)
    return string.deserialize()
}

inline fun <reified T : Any> T.toJsonPrimitive(json: Json = JsonConfigurator.json): JsonPrimitive {
    val string = serialize(json)
    return string.deserialize()
}