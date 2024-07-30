package io.kamo.ktor.client.ai.core.util

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.json.*
import kotlinx.serialization.serializer

@OptIn(ExperimentalSerializationApi::class)
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
@OptIn(InternalSerializationApi::class)
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