package org.krosai.core.embedding.model

import org.krosai.core.embedding.metadata.EmbeddingResultMetadata
import org.krosai.core.model.ModelResult

data class Embedding(
    override val output: List<Double>,
    val index: Int,
    override val metadata: EmbeddingResultMetadata = EmbeddingResultMetadata()
): ModelResult<List<Double>>