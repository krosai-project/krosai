package io.kamo.ktor.client.ai.core.chat.function

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.StructureKind
import kotlinx.serialization.descriptors.elementDescriptors
import kotlinx.serialization.json.*
import kotlinx.serialization.serializer
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

    var inputSchema: String = ""

    @OptIn(InternalSerializationApi::class)
    fun build(): FunctionCall {
        requireNotNull(call) { "call must be not null" }
        requireNotNull(name) { "name must be not null" }

        // resolve input schema
        if (inputSchema.isEmpty() && needResolveTypeSchema(inputType)) {
            inputType.serializer().let {
                inputSchema = resolveTypeSchema(it.descriptor).let(::createJsonElement).toString()
            }
        }

        return SimpleFunctionCall(
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
            val element = jsonConverter.parseToJsonElement(it)
            // compatible with only one parameter
            val realElement = when (element) {

                is JsonArray -> if (element.size == 1 && I::class.simpleName != Array::class.simpleName)
                    element.first() else element

                is JsonObject -> if (element.size == 1)
                    element.values.first() else element

                else -> element
            }
            jsonConverter.decodeFromJsonElement<I>(realElement)
        }

        this.call = { call(it as I) }
        return this
    }


    companion object {

        @OptIn(ExperimentalSerializationApi::class)
        val DefaultJsonConverter = Json {
            ignoreUnknownKeys = true
            explicitNulls = false
        }

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


/**
 * Resolve type schema from SerialDescriptor
 */
@OptIn(ExperimentalSerializationApi::class)
internal fun resolveTypeSchema(
    descriptor: SerialDescriptor,
): Map<String, Any> {
    val result: MutableMap<String, Any> = mutableMapOf()
    descriptor.elementDescriptors.forEachIndexed { i, element ->
        val name = descriptor.getElementName(i)
        result[name] = mutableMapOf<String, Any>(
            "type" to element.serialName,
            "kind" to element.kind.toString(),
            "isNullable" to element.isNullable,
            "kind" to when (element.kind) {
                is PrimitiveKind -> element.kind.toString()
                else -> element.kind.toString()
            }
        ).apply {
            if (element.kind is StructureKind.CLASS) put("parameters", resolveTypeSchema(element))
        }

    }
    return result
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