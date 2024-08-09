package io.github.krosai.core.embedding.model

import io.github.krosai.core.model.ModelResponse
import io.github.krosai.core.model.ResponseMetadata

data class EmbeddingResponse(
    override val results: List<Embedding>,
    override val responseMetadata: ResponseMetadata
): ModelResponse<Embedding> {

    override val result: Embedding
        get() = results.first()
}