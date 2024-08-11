package io.github.krosai.core.chat.metadata

import io.github.krosai.core.model.ResponseMetadata

data class ChatResponseMetadata(
    val id: String = "",
    val model: String = "",
    val rateLimit: RateLimit = RateLimit.EMPTY,
    val usage: Usage = Usage.EMPTY,
    private val metadata: MutableMap<String, Any> = hashMapOf()
) : ResponseMetadata, Map<String, Any> by metadata