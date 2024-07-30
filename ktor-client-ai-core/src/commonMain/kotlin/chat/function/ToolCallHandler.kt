package io.kamo.ktor.client.ai.core.chat.function

import chat.function.ToolResponse
import io.kamo.ktor.client.ai.core.chat.message.Message

interface ToolCallHandler {

    fun executeFunctionCall(getFunctionCall: (Set<String>) -> List<FunctionCall>, assistantMessage: Message.Assistant): Message.Tool {
        val functionCalls = getFunctionCall(assistantMessage.toolCall.map { it.name }.toSet())
            .associateBy { it.name }

        return Message.Tool(
            assistantMessage.toolCall.map { toolCall ->
                ToolResponse(
                    id = toolCall.id,
                    name = toolCall.name,
                    responseData = functionCalls[toolCall.name]!!.call(toolCall.arguments)
                )
            }
        )
    }
}