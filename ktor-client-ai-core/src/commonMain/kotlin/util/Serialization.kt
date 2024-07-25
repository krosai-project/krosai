package io.kamo.ktor.client.ai.core.util

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.StructureKind
import kotlinx.serialization.descriptors.elementDescriptors

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
