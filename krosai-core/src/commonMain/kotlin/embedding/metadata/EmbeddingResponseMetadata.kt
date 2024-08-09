package io.github.krosai.core.embedding.metadata

import io.github.krosai.core.chat.metadata.Usage
import io.github.krosai.core.model.ResponseMetadata

data class EmbeddingResponseMetadata(
    val model: String,
    val usage: Usage,
    val metadata: Map<String, Any> = mapOf<String, Any>()
): ResponseMetadata
