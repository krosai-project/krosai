package io.github.krosai.core.image

/**
 * Represents an image message.
 *
 * @property text The text content of the image message.
 * @property weight The weight of the image message, represented as a float. Default value is null.
 *
 * @author KAMOsama
 */
data class ImageMessage(
    val text: String,
    val weight: Float? = null,
)