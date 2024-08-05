package io.kamo.ktor.client.ai.core.chat.function

import chat.function.ToolResponse
import io.kamo.ktor.client.ai.core.chat.message.Message

interface ToolCallHandler {

    fun executeFunctionCall(
        assistantMessage: Message.Assistant,
        getFunctionCall: (Set<String>) -> List<FunctionCall>
    ): Message.Tool {
        val toolCallNames = assistantMessage.toolCall?.map { it.name }?.toSet() ?: emptySet()
        val functionCalls = getFunctionCall(toolCallNames)
            .associateBy { it.name }

        val toolResponses = assistantMessage.toolCall?.map { toolCall ->
            ToolResponse(
                id = toolCall.id,
                name = toolCall.name,
                responseData = functionCalls[toolCall.name]!!.call(toolCall.arguments)
            )
        }
        return Message.Tool(
            toolResponses ?: emptyList()
        )
    }
}