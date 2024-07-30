@file:Suppress("unused")

package io.kamo.ktor.client.ai.core.chat.message

import chat.function.ToolResponse
import io.kamo.ktor.client.ai.core.chat.function.ToolCall

sealed class Message(
    val type: MessageType,
    val content: String,
) {
    class System(content: String) : Message(MessageType.SYSTEM, content)

    class User(content: String) : Message(MessageType.USER, content)

    class Assistant(
        content: String,
        val properties: Map<String, Any> = mapOf(),
        val toolCall: List<ToolCall> = listOf()
    ) : Message(MessageType.ASSISTANT, content)

    class Tool(
        val toolResponses: List<ToolResponse>
    ) : Message(MessageType.TOOL, "")

}


