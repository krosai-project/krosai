@file:Suppress("unused")
package io.kamo.ktor.client.ai.core.chat.function

import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.jsonObject


inline fun <reified T> FunctionCallBuilder.decodeFromJsonElement(element: JsonElement) =
    jsonConverter.decodeFromJsonElement<T>(element)

inline fun <reified I1 : Any, reified I2 : Any> FunctionCallBuilder.withCall(
    noinline call: (I1, I2) -> Any
): FunctionCallBuilder {
    return withCall<JsonElement> {
        with(it.jsonObject.values.iterator()) {
            call(
                decodeFromJsonElement(next()),
                decodeFromJsonElement(next())
            )
        }
    }
}

inline fun <reified I1 : Any, reified I2 : Any, reified I3 : Any> FunctionCallBuilder.withCall(
    noinline call: (I1, I2, I3) -> Any
): FunctionCallBuilder {
    return withCall<JsonElement> {
        with(it.jsonObject.values.iterator()) {
            call(
                decodeFromJsonElement(next()),
                decodeFromJsonElement(next()),
                decodeFromJsonElement(next())
            )
        }
    }
}

inline fun <reified I1 : Any, reified I2 : Any, reified I3 : Any, reified I4 : Any> FunctionCallBuilder.withCall(
    noinline call: (I1, I2, I3, I4) -> Any
): FunctionCallBuilder {
    return withCall<JsonElement> {
        with(it.jsonObject.values.iterator()) {
            call(
                decodeFromJsonElement(next()),
                decodeFromJsonElement(next()),
                decodeFromJsonElement(next()),
                decodeFromJsonElement(next())
            )
        }
    }
}

inline fun <reified I1 : Any, reified I2 : Any, reified I3 : Any, reified I4 : Any, reified I5 : Any> FunctionCallBuilder.withCall(
    noinline call: (I1, I2, I3, I4, I5) -> Any
): FunctionCallBuilder {
    return withCall<JsonElement> {
        with(it.jsonObject.values.iterator()) {
            call(
                decodeFromJsonElement(next()),
                decodeFromJsonElement(next()),
                decodeFromJsonElement(next()),
                decodeFromJsonElement(next()),
                decodeFromJsonElement(next())
            )
        }
    }
}


