package io.github.krosai.openai.model

import io.github.krosai.core.image.*
import io.github.krosai.core.util.merge
import io.github.krosai.openai.api.OpenAiApi
import io.github.krosai.openai.api.image.OpenAiImageRequest
import io.github.krosai.openai.api.image.OpenAiImageResponse
import io.github.krosai.openai.metadata.OpenAiImageGenerationMetadata
import io.github.krosai.openai.options.OpenAiImageOptions

class OpenAiImageModel(
    private val api: OpenAiApi,
    private val defaultOptions: OpenAiImageOptions = OpenAiImageOptions()
) : ImageModel {
    override suspend fun call(prompt: ImagePrompt): ImageResponse {
        val text = prompt.instructions.first().text
        var request = OpenAiImageRequest(text, defaultOptions.model)
        request = defaultOptions.merge(request)
        val openAiImageOptions = prompt.options as OpenAiImageOptions
        request = openAiImageOptions.merge(request)

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

