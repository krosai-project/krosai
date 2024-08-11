package io.github.krosai.core.embedding.metadata

import io.github.krosai.core.chat.metadata.Usage
import io.github.krosai.core.model.ResponseMetadata

data class EmbeddingResponseMetadata(
    val model: String,
    val usage: Usage,
    private val metadata: Map<String, Any> = mapOf()
) : ResponseMetadata, Map<String, Any> by metadata
