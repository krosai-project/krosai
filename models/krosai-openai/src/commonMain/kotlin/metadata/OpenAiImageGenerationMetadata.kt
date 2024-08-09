package io.github.krosai.openai.metadata

import io.github.krosai.core.image.ImageGenerationMetadata

data class OpenAiImageGenerationMetadata(
    val revisedPrompt: String
) : ImageGenerationMetadata