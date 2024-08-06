package io.github.krosai.client.ai.core.chat.function

import io.github.krosai.client.ai.core.util.DefaultJsonConverter
import io.github.krosai.client.ai.core.util.createJsonElement
import io.github.krosai.client.ai.core.util.resolveTypeSchema
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.json.*
import kotlinx.serialization.serializer
import kotlinx.serialization.serializerOrNull
import kotlin.reflect.KClass
import kotlin.reflect.KFunction

class FunctionCallBuilder @PublishedApi internal constructor(
    var name: String? = null,
    var description: String
) {

    var jsonConverter: Json = DefaultJsonConverter

    @PublishedApi
    internal var inputType: KClass<*> = JsonElement::class

    @PublishedApi
    internal var inputConverter: Func1<String, Any> = jsonConverter::parseToJsonElement

    @PublishedApi
    internal var call: Func1<Any, Any>? = null

    private var outputConverter: Func1<Any, String> = Any::toString

    var inputSchema: JsonElement = JsonNull

    fun build(): FunctionCall {
        requireNotNull(call) { "call must be not null" }
        requireNotNull(name) { "name must be not null" }

        // resolve input schema
        if (inputSchema is JsonNull && needResolveTypeSchema(inputType)) {
            inputType.serializer().let {
                inputSchema = resolveTypeSchema(it.descriptor).let(::createJsonElement)
            }
        }

        return GenerationFunctionCall(
            name = name!!,
            description = description,
            inputConverter = inputConverter,
            outputConverter = outputConverter,
            inputSchema = inputSchema,
            call = call!!
        )

    }

    inline fun <reified I : Any> withCall(
        noinline call: (I) -> Any
    ): FunctionCallBuilder {
        this.inputType = I::class

        this.inputConverter = {
            @Suppress
            val element = jsonConverter.parseToJsonElement(it)
            // compatible with only one parameter
            // TODO: support multiple parameters
            val realElement = when (element) {

                is JsonArray -> if (element.size == 1 && I::class.simpleName != Array::class.simpleName)
                    element.first() else element

                is JsonObject -> if (element.size == 1 && I::class.serializerOrNull()?.descriptor?.kind is PrimitiveKind)
                    element.values.first() else element

                else -> element
            }
            jsonConverter.decodeFromJsonElement<I>(realElement)
        }

        this.call = { call(it as I) }
        return this
    }


    companion object {

        private val excludeTypes = listOf(
            Array::class,
            Collection::class,
            Map::class,
            JsonElement::class
        )

        /**
         * Check if type is need to resolve schema.
         * inputType must be an independent type (example: data class) and marked as [kotlinx.serialization.Serializable].
         */
        @OptIn(InternalSerializationApi::class)
        fun needResolveTypeSchema(inputType: KClass<*>): Boolean =
            excludeTypes.all { it != inputType } && runCatching { inputType.serializer() }.isSuccess
    }

}

inline fun buildFunctionCall(
    name: String? = null,
    description: String = "",
    builder: FunctionCallBuilder.() -> Unit
): FunctionCall = FunctionCallBuilder(name, description).apply(builder).build()

inline fun buildFunctionCall(
    name: KFunction<*>,
    description: String = "",
    builder: FunctionCallBuilder.() -> Unit
): FunctionCall = buildFunctionCall(name.name, description, builder)


