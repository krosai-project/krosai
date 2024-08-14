package org.krosai.core.image

import org.krosai.core.model.ModelResponse

/**
 * Represents the response from an image generation AI model. It contains the list of generated image generations,
 * as well as the response metadata associated with the AI model's response.
 *
 * @property results The list of generated image generations.
 * @property responseMetadata The response metadata associated with the AI model's response.
 * @constructor Creates an instance of [ImageResponse] with the given parameters.
 *
 * @see ModelResponse
 * @see ImageGeneration
 * @see ImageResponseMetadata
 *
 * @author KAMOsama
 */
data class ImageResponse(
    override val results: List<ImageGeneration>,
    override val responseMetadata: ImageResponseMetadata = ImageResponseMetadata()
) : ModelResponse<ImageGeneration> {
    override val result: ImageGeneration
        get() = results.first()
}
