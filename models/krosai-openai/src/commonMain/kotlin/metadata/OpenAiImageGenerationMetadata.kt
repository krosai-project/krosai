package org.krosai.openai.metadata

import org.krosai.core.image.ImageGenerationMetadata

data class OpenAiImageGenerationMetadata(
    val revisedPrompt: String
) : ImageGenerationMetadata