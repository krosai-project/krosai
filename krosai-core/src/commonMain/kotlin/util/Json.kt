package org.krosai.core.util

import kotlinx.serialization.json.*
import kotlinx.serialization.serializer

val DefaultJsonConverter = Json {
    ignoreUnknownKeys = true
    explicitNulls = false
    encodeDefaults = true
    isLenient = true
    allowSpecialFloatingPointValues = true
    allowStructuredMapKeys = true
    prettyPrint = false
    useArrayPolymorphism = false
}


/**
 * Create JsonElement from any
 */
internal fun createJsonElement(any: Any): JsonElement {
    return when (any) {
        is Map<*, *> -> buildJsonObject {
            any.forEach { (key, value) ->
                put(key.toString(), createJsonElement(value!!))
            }
        }

        is Collection<*> -> buildJsonArray {
            any.forEach {
                add(createJsonElement(it!!))
            }
        }

        is Array<*> -> buildJsonArray {
            any.forEach {
                add(createJsonElement(it!!))
            }
        }

        is JsonElement -> any
        is Number -> JsonPrimitive(any)
        is Boolean -> JsonPrimitive(any)
        is String -> JsonPrimitive(any)
        else -> createJsonElement(resolveTypeSchema(any::class.serializer().descriptor))
    }
}