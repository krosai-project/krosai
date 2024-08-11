package io.github.krosai.openai.model

import io.github.krosai.core.image.*
import io.github.krosai.core.util.mergeElement
import io.github.krosai.openai.api.OpenAiApi
import io.github.krosai.openai.api.image.OpenAiImageModelEnum
import io.github.krosai.openai.api.image.OpenAiImageRequest
import io.github.krosai.openai.api.image.OpenAiImageResponse
import io.github.krosai.openai.metadata.OpenAiImageGenerationMetadata
import io.github.krosai.openai.options.OpenAiImageOptions

class OpenAiImageModel(
    private val api: OpenAiApi,
    private val defaultOptions: OpenAiImageOptions = OpenAiImageOptions()
) : ImageModel {
    override suspend fun call(prompt: ImagePrompt): ImageResponse {
        val mergedOptions = prompt.options.merge(defaultOptions)
        val request = createRequest(prompt, mergedOptions)

        val response = api.createImage(request)

        return response.toImageResponse()
    }
}

fun OpenAiImageResponse.toImageResponse(): ImageResponse {
    val imageGenerations = this.data.map {
        ImageGeneration(
            Image(it.url, it.b64Json.orEmpty()),
            OpenAiImageGenerationMetadata(it.revisedPrompt.orEmpty())
        )
    }
    return ImageResponse(imageGenerations, ImageResponseMetadata(this.created))
}

private fun createRequest(prompt: ImagePrompt, options: OpenAiImageOptions): OpenAiImageRequest {
    val text = prompt.instructions.first().text
    val request = OpenAiImageRequest(
        text,
        OpenAiImageModelEnum.DEFAULT.model
    )
    return request.mergeElement(options)
}

private fun ImageOptions.merge(defaultOptions: OpenAiImageOptions): OpenAiImageOptions {
    this.height
    return OpenAiImageOptions(
        n = this.n ?: defaultOptions.n,
        model = this.model ?: defaultOptions.model,
        width = this.width ?: defaultOptions.width,
        height = this.height ?: defaultOptions.height,
        quality = defaultOptions.quality,
        responseFormat = this.responseFormat ?: defaultOptions.responseFormat,
        style = this.style ?: defaultOptions.style,
        user = defaultOptions.user
    )

}