package io.github.krosai.core.embedding.model

import io.github.krosai.core.model.ModelRequest

data class EmbeddingRequest(
    override val instructions: List<String>,
    override val options: EmbeddingOptions
): ModelRequest<List<String>>