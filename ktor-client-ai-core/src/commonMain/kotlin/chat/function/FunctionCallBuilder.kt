package io.kamo.ktor.client.ai.core.chat.function

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlin.reflect.KClass

class FunctionCallBuilder(
    private val name: String,
    private val description: String
) {
    @PublishedApi
    internal var inputType: KClass<*> = JsonElement::class

    @PublishedApi
    internal var inputConverter: Func1<String, Any> = Json.Default::parseToJsonElement

    @PublishedApi
    internal var call: Func1<Any, Any>? = null

    private var outputConverter: Func1<Any, String> = Any::toString

    var inputSchema: String = ""

    fun build(): FunctionCall {
        requireNotNull(call)
        return SimpleFunctionCall(
            name = name,
            description = description,
            inputConverter = inputConverter,
            outputConverter = outputConverter,
            inputSchema = inputSchema,
            call = call!!
        )

    }

    fun <I : Any> FunctionCallBuilder.withCall(
        inputType: KClass<I>,
        call: Func1<I, Any>
    ): FunctionCallBuilder {
        this.inputType = inputType
        this.inputConverter = { Json.decodeFromString(it) }
        this.call = { call(it as I) }
        return this
    }
}