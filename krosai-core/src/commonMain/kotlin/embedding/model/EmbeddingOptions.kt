package org.krosai.core.embedding.model

import org.krosai.core.model.ModelOptions

interface EmbeddingOptions: ModelOptions {
    val model: String?
    val dimension: Int?
}

class DefaultEmbeddingOptions(
    override val model: String? = null,
    override val dimension: Int? = null
): EmbeddingOptions