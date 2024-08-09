package io.github.krosai.core.image

import io.github.krosai.core.model.ResponseMetadata

/**
 * Represents the metadata associated with the image response.
 * It provides additional information about the response generated by the AI model,
 * including the creation timestamp of the generated image.
 *
 * @property created The timestamp when the image response was created, in milliseconds.
 * @constructor Creates an instance of [ImageResponseMetadata] with the given parameters.
 *
 * @see ResponseMetadata
 *
 * @author KAMOsama
 */
class ImageResponseMetadata(
    // TODO: Add system currentTimeMillis
    val created: Long = -1,
) : ResponseMetadata, MutableMap<String, Any> by hashMapOf()