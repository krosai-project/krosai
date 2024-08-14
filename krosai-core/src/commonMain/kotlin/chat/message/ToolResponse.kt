package org.krosai.core.chat.message

/**
 * Represents the response from a tool call.
 *
 * @property id The unique identifier of the tool response.
 * @property name The name of the tool response.
 * @property responseData The data returned by the tool response.
 *
 * @author Ikutsu
 */
data class ToolResponse(
    val id: String,
    val name: String,
    val responseData: String
)