package io.github.krosai.core.embedding.model

import io.github.krosai.core.embedding.metadata.EmbeddingResultMetadata
import io.github.krosai.core.model.ModelResult

data class Embedding(
    override val output: List<Double>,
    val index: Int,
    override val metadata: EmbeddingResultMetadata = EmbeddingResultMetadata()
): ModelResult<List<Double>>