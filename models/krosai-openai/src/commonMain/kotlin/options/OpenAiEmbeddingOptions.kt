package org.krosai.openai.options

import org.krosai.core.embedding.model.EmbeddingOptions
import org.krosai.openai.api.embedding.OpenAiEmbeddingModelEnum

data class OpenAiEmbeddingOptions(
    override var model: String = OpenAiEmbeddingModelEnum.DEFAULT.model,
    var encodingFormat: String? = null,
    override var dimension: Int? = null,
    var user: String? = null
): EmbeddingOptions