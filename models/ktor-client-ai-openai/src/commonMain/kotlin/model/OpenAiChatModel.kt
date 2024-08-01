package io.kamo.ktor.client.ai.openai.model

import io.kamo.ktor.client.ai.core.chat.function.FunctionCall
import io.kamo.ktor.client.ai.core.chat.function.FunctionCallOptions
import io.kamo.ktor.client.ai.core.chat.function.ToolCall
import io.kamo.ktor.client.ai.core.chat.function.ToolCallHandler
import io.kamo.ktor.client.ai.core.chat.message.Message
import io.kamo.ktor.client.ai.core.chat.message.MessageType
import io.kamo.ktor.client.ai.core.chat.model.ChatModel
import io.kamo.ktor.client.ai.core.chat.model.ChatResponse
import io.kamo.ktor.client.ai.core.chat.model.Generation
import io.kamo.ktor.client.ai.core.chat.prompt.ChatOptions
import io.kamo.ktor.client.ai.core.chat.prompt.Prompt
import io.kamo.ktor.client.ai.openai.api.*
import io.kamo.ktor.client.ai.openai.api.ChatCompletionRequest.FunctionTool
import io.kamo.ktor.client.ai.openai.options.OpenAiChatOptions
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

/** OpenAI Chat API implementation. */



class OpenAiChatModel(
    private val chatOptions: OpenAiChatOptions,
    private val api: OpenAiApi,
    private val getFunctionCallNames: (Set<String>) -> List<FunctionCall>
) : ToolCallHandler, ChatModel {

    override val defaultChatOptions: ChatOptions get() = chatOptions.copy()

    override suspend fun call(prompt: Prompt): ChatResponse {
        val request = createRequest(prompt, false, getFunctionCallNames)
        return api.call(request)
            .toChatResponse()
            .let { chatResponse ->
                if (isToolCall(chatResponse)) {
                    val currentPrompt = prompt.copy(instructions = processToolCall(prompt, chatResponse))
                    call(currentPrompt)
                } else {
                    chatResponse
                }
            }
    }

    override suspend fun stream(prompt: Prompt): Flow<ChatResponse> {
        val request = createRequest(prompt, true, getFunctionCallNames)
        api.stream(request)
            .map {
                it
                    .toChatCompletion()
                    .toChatResponse()
            }.let { chatResponse ->
                val response = chatResponse.first()
                return if (isToolCall(response)) {
                    val currentPrompt = prompt.copy(instructions = processToolCall(prompt, response))
                    stream(currentPrompt)
                } else {
                    chatResponse
                }
            }

    }

    private fun isToolCall(chatResponse: ChatResponse): Boolean {
        return chatResponse.result.output.toolCall.isNotEmpty()
    }

    private fun processToolCall(prompt: Prompt, chatResponse: ChatResponse): List<Message> {
        return buildToolCallMessage(
            prompt.instructions,
            chatResponse.result.output,
            executeFunctionCall(chatResponse.result.output) {
                // TODO refactor
                val options = prompt.options
                if (options is FunctionCallOptions) {
                    getFunctionCallNames(it) + options.functionCalls
                } else {
                    getFunctionCallNames(it)
                }
            }
        )
    }

    private fun buildToolCallMessage(
        messageContext: List<Message>,
        assistantMessage: Message.Assistant,
        toolMessage: Message.Tool
    ): List<Message> {
        return messageContext + assistantMessage + toolMessage
    }
}

fun ChatCompletionChunk.toChatCompletion(): ChatCompletion {
    val choices = choices.map { cc ->
        ChatCompletion.Choice(cc.finishReason, cc.index, cc.delta, cc.logprobs)
    }
    return ChatCompletion(
        id = id,
        choices = choices,
        created = created,
        model = model,
        systemFingerprint = systemFingerprint,
        obj = "chat.completion",
        usage = usage,
    )
}

fun ChatCompletion.toChatResponse(): ChatResponse {
    return ChatResponse(
        choices.map { choice ->
            Generation(
                Message.Assistant(
                    content = choice.message.content.orEmpty(),
                    toolCall = choice.message.toolCalls.orEmpty().map { toolCall ->
                        ToolCall(
                            toolCall.id,
                            toolCall.function.name,
                            "function",
                            toolCall.function.arguments
                        )
                    }
                )
            )
        }
    )
}

private fun createRequest(
    prompt: Prompt,
    stream: Boolean,
    getFunctionCallNames: (Set<String>) -> List<FunctionCall>
): ChatCompletionRequest {
    val chatCompletionMessages = prompt.instructions.flatMap { message ->
        when (message.type) {
            MessageType.USER, MessageType.SYSTEM -> {
                listOf(
                    ChatCompletionMessage(
                        message.content,
                        ChatCompletionMessage.Role.fromMessageType(message.type)
                    )
                )
            }

            MessageType.ASSISTANT -> {
                val assistantMessage = message as Message.Assistant
                val toolCalls = assistantMessage.toolCall.map {
                    ChatCompletionMessage.ToolCall(
                        it.id,
                        it.type,
                        ChatCompletionMessage.ChatCompletionFunction(
                            it.name,
                            it.arguments
                        )
                    )
                }
                listOf(
                    ChatCompletionMessage(
                        content = message.content,
                        role = ChatCompletionMessage.Role.ASSISTANT,
                        toolCalls = toolCalls
                    )
                )
            }

            MessageType.TOOL -> {
                val toolMessage = message as Message.Tool
                toolMessage.toolResponses.map {
                    ChatCompletionMessage(
                        it.responseData,
                        ChatCompletionMessage.Role.TOOL,
                        it.name,
                        it.id
                    )
                }
            }
        }
    }

    val chatOptions = prompt.options as OpenAiChatOptions

    val toolCalls = (getFunctionCallNames(chatOptions.functionNames) + chatOptions.functionCalls).map {
        FunctionTool(
            FunctionTool.Function(
                it.description,
                it.name,
                it.inputSchema
            )
        )
    }

    return ChatCompletionRequest(
        chatCompletionMessages,
        chatOptions.model,
        stream = stream,
        tools = toolCalls.takeIf { it.isNotEmpty() },
    )
}
//{
//  "id": "chatcmpl-9hBQDNOWPnolxTAHvScpoE4BltBR8",
//  "object": "chat.completion",
//  "created": 1720079037,
//  "model": "gpt-4o-2024-05-13",
//  "choices": [
//    {
//      "index": 0,
//      "message": {
//        "role": "assistant",
//        "content": "你好！有什么我可以帮你的吗？"
//      },
//      "logprobs": null,
//      "finish_reason": "stop"
//    }
//  ],
//  "usage": {
//    "prompt_tokens": 8,
//    "completion_tokens": 9,
//    "total_tokens": 17
//  },
//  "system_fingerprint": "fp_d576307f90"
//}