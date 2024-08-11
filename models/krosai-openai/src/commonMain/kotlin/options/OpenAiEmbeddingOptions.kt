package io.github.krosai.openai.options

import io.github.krosai.core.embedding.model.EmbeddingOptions
import io.github.krosai.openai.api.embedding.OpenAiEmbeddingModelEnum

data class OpenAiEmbeddingOptions(
    override var model: String = OpenAiEmbeddingModelEnum.DEFAULT.model,
    var encodingFormat: String? = null,
    override var dimension: Int? = null,
    var user: String? = null
): EmbeddingOptions