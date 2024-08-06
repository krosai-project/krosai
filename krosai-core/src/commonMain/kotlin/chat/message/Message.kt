@file:Suppress("unused")

package io.github.krosai.core.chat.message

import io.github.krosai.core.chat.function.ToolCall
import io.github.krosai.core.chat.function.ToolResponse

sealed class Message(
    val type: MessageType,
    open val content: String,
) {
    data class System(
        override val content: String
    ) : Message(MessageType.SYSTEM, content)

    data class User(
        override val content: String
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


