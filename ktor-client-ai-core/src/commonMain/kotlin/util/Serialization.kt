package io.kamo.ktor.client.ai.core.util

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.descriptors.*

/**
 * Resolve type schema from SerialDescriptor
 */
@OptIn(ExperimentalSerializationApi::class)
internal fun resolveTypeSchema(
    descriptor: SerialDescriptor,
): Map<String, Any> {
    val result: MutableMap<String, Any> = mutableMapOf()
    result["\$schema"] = "https://json-schema.org/draft/2020-12/schema"
    result["type"] = "object"
    val required = mutableListOf<String>()
    result["required"] = required
    descriptor.annotations.filterIsInstance<SerialDescription>().firstOrNull()?.let {
        result["description"] = it.value
    }

    descriptor.elementDescriptors.toList().takeIf { it.isNotEmpty() }?.let { descriptorList ->
        val properties = mutableMapOf<String, Any>()
        result["properties"] = properties
        descriptorList.forEachIndexed { i, element ->
            val parameter = mutableMapOf<String, Any>()
            val name = descriptor.getElementName(i)
            properties[name] = parameter
            descriptor.getElementAnnotations(i).firstOrNull { it is SerialDescription }?.let {
                parameter["description"] = (it as SerialDescription).value
            }

            val kind = element.kind
            parameter["type"] = when (kind) {

                is PrimitiveKind -> kind.toString().lowercase()


                SerialKind.ENUM -> {
                    kind.toString().lowercase()
                }

                StructureKind.CLASS -> {
                    "object"
                }

                else -> TODO("support other kind")
            }

        }
    }
    return result
}

fun doResolveElement(
    descriptor: SerialDescriptor,
): Any {
    when (descriptor.kind) {
        PolymorphicKind.OPEN -> TODO()
        PolymorphicKind.SEALED -> TODO()
        PrimitiveKind.BOOLEAN -> TODO()
        PrimitiveKind.BYTE -> TODO()
        PrimitiveKind.CHAR -> TODO()
        PrimitiveKind.DOUBLE -> TODO()
        PrimitiveKind.FLOAT -> TODO()
        PrimitiveKind.INT -> TODO()
        PrimitiveKind.LONG -> TODO()
        PrimitiveKind.SHORT -> TODO()
        PrimitiveKind.STRING -> TODO()
        SerialKind.CONTEXTUAL -> TODO()
        SerialKind.ENUM -> TODO()
        StructureKind.CLASS -> TODO()
        StructureKind.LIST -> TODO()
        StructureKind.MAP -> TODO()
        StructureKind.OBJECT -> TODO()
    }
}