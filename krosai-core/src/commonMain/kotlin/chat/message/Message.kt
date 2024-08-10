@file:Suppress("unused")

package io.github.krosai.core.chat.message


/**
 * Represents a message.
 *
 * @property type The type of the message.
 * @property content The content of the message.
 *
 * @author KAMOsama
 */
sealed class Message(
    val type: MessageType,
    open val content: String,
) {
    data class System(
        override val content: String
    ) : Message(MessageType.SYSTEM, content)

    data class User(
        override val content: String,
        val media: List<Media<*>> = emptyList()
    ) : Message(MessageType.USER, content)

    data class Assistant(
        override val content: String,
        val properties: Map<String, Any> = mapOf(),
        val toolCall: List<ToolCall>? = null
    ) : Message(MessageType.ASSISTANT, content)

    class Tool(
        val toolResponses: List<ToolResponse>
    ) : Message(MessageType.TOOL, "")

}


