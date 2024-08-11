package io.github.krosai.core.chat.message

/**
 * Represents a tool call made by the Assistant.
 *
 * @property id The unique identifier of the tool call.
 * @property name The name of the tool call.
 * @property type The type of the tool call. Always "function".
 * @property arguments The arguments passed to the tool call as a String.
 *
 * @author Ikutsu
 */
data class ToolCall(
    val id: String,
    val name: String,
    val type: String = "function",
    val arguments: String
)