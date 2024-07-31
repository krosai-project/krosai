package io.kamo.ktor.client.ai.core.chat.function

import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull

class GenerationFunctionCall<in I : Any, out O : Any>(
    name: String,
    description: String,
    inputConverter: Func1<String, I>,
    outputConverter: Func1<O, String> = Any::toString,
    inputSchema: JsonElement = JsonNull,
    call: Func1<I, O>
) : AbstractFunctionCall<I, O>(name, description, inputSchema, inputConverter, outputConverter),
    Func1<I, O> by call