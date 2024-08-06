package io.github.krosai.core.chat.function

data class ToolResponse(
    val id: String,
    val name: String,
    val responseData: String
)