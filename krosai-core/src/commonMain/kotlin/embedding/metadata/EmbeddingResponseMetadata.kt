package org.krosai.core.embedding.metadata

import org.krosai.core.chat.metadata.Usage
import org.krosai.core.model.ResponseMetadata

data class EmbeddingResponseMetadata(
    val model: String,
    val usage: Usage,
    private val metadata: Map<String, Any> = mapOf()
) : ResponseMetadata, Map<String, Any> by metadata
