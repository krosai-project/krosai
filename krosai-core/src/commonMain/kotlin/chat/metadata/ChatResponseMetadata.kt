package org.krosai.core.chat.metadata

import org.krosai.core.model.ResponseMetadata

data class ChatResponseMetadata(
    val id: String = "",
    val model: String = "",
    val rateLimit: RateLimit = RateLimit.Empty,
    val usage: Usage = Usage.Empty,
    private val metadata: MutableMap<String, Any> = hashMapOf()
) : ResponseMetadata, Map<String, Any> by metadata