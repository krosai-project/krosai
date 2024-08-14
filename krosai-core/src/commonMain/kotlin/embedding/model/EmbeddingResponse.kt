package org.krosai.core.embedding.model

import org.krosai.core.model.ModelResponse
import org.krosai.core.model.ResponseMetadata

data class EmbeddingResponse(
    override val results: List<Embedding>,
    override val responseMetadata: ResponseMetadata
): ModelResponse<Embedding> {

    override val result: Embedding
        get() = results.first()
}