package io.github.krosai.openai.options

import io.github.krosai.core.embedding.model.EmbeddingOptions

data class OpenAiEmbeddingOptions(
    override var model: String? = null,
    var encodingFormat: String? = null,
    override var dimension: Int? = null,
    var user: String? = null
): EmbeddingOptions