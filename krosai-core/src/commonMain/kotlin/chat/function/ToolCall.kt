package io.github.krosai.client.ai.core.chat.function

data class ToolCall(
    val id: String,
    val name: String,
    val type: String,
    val arguments: String
)
