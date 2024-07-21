package io.kamo.ktor.client.ai.core.chat.function

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.jsonObject

inline fun <reified I1 : Any, reified I2 : Any> FunctionCallBuilder.withCall(
    noinline call: (I1, I2) -> Any
): FunctionCallBuilder {
    return withCall(JsonElement::class) {
        val params = it.jsonObject.values.toList()
        call(
            Json.decodeFromJsonElement(params[0]),
            Json.decodeFromJsonElement(params[1])
        )
    }
}

inline fun <reified I1 : Any, reified I2 : Any, reified I3 : Any> FunctionCallBuilder.withCall(
    noinline call: (I1, I2, I3) -> Any
): FunctionCallBuilder {
    return withCall(JsonElement::class) {
        val params = it.jsonObject.values.toList()
        call(
            Json.decodeFromJsonElement(params[0]),
            Json.decodeFromJsonElement(params[1]),
            Json.decodeFromJsonElement(params[2])
        )
    }
}
