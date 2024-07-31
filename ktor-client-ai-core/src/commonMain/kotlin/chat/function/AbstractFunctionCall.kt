package io.kamo.ktor.client.ai.core.chat.function

import kotlinx.serialization.json.JsonElement

abstract class AbstractFunctionCall<in I : Any, out O : Any>(
    override val name: String,
    override val description: String,
    override val inputSchema: JsonElement,
    private val inputConverter: Func1<String, I>,
    private val outputConverter: Func1<O, String>,
) : FunctionCall, Func1<I, O> {

    override fun call(req: String): String =
        inputConverter(req).let(::invoke).let(outputConverter)

}

typealias Func1<I, O> = (I) -> O
