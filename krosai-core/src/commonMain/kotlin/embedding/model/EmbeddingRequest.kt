package org.krosai.core.embedding.model

import org.krosai.core.model.ModelRequest

data class EmbeddingRequest(
    override val instructions: List<String>,
    override val options: EmbeddingOptions
): ModelRequest<List<String>>