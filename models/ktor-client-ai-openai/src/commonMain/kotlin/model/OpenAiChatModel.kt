package io.kamo.ktor.client.ai.openai.model

import io.kamo.ktor.client.ai.core.chat.function.FunctionCall
import io.kamo.ktor.client.ai.core.chat.function.ToolCall
import io.kamo.ktor.client.ai.core.chat.function.ToolCallHandler
import io.kamo.ktor.client.ai.core.chat.message.Message
import io.kamo.ktor.client.ai.core.chat.model.ChatModel
import io.kamo.ktor.client.ai.core.chat.model.ChatResponse
import io.kamo.ktor.client.ai.core.chat.model.Generation
import io.kamo.ktor.client.ai.core.chat.prompt.ChatOptions
import io.kamo.ktor.client.ai.core.chat.prompt.Prompt
import io.kamo.ktor.client.ai.openai.api.ChatCompletion
import io.kamo.ktor.client.ai.openai.api.ChatCompletionChunk
import io.kamo.ktor.client.ai.openai.api.ChatCompletionRequest
import io.kamo.ktor.client.ai.openai.api.OpenAiApi
import io.kamo.ktor.client.ai.openai.options.OpenAiChatOptions
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.sse.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.flow.*
import kotlinx.serialization.json.Json

/** OpenAI Chat API implementation. */

/**
 * Whether to stream back partial progress.
 * If set, tokens will be sent as data-only server-sent events as they become available,
 * with the stream terminated by a data: [[DONE]] message.
 */
private val SSE_PREDICATE: (String) -> Boolean = { it.isNotEmpty() && it != "[DONE]" }

class OpenAiChatModel(
    private val chatOptions: OpenAiChatOptions,
    private val api: OpenAiApi,
    private val getFunctionCallNames: (Set<String>) -> List<FunctionCall>
) : ToolCallHandler, ChatModel {

    override val defaultChatOptions: ChatOptions get() = chatOptions.copy()

    override suspend fun call(request: Prompt): ChatResponse {
        api.call(request).body<ChatCompletion>().toChatResponse().let { chatResponse ->
            if (isToolCall(chatResponse)) {
                return call(
                    request.copy(
                        instructions = processToolCall(request, chatResponse),
                    )
                )
            } else {
                return chatResponse
            }
        }
    }

    override suspend fun stream(request: Prompt): Flow<ChatResponse> {
        api.stream(request).incoming
            .mapNotNull { it.data }
            .filter(SSE_PREDICATE)
            .map {
                Json.decodeFromString<ChatCompletionChunk>(it)
                    .toChatCompletion()
                    .toChatResponse()
            }.let {
                if (isToolCall(it.first())) {
                    return stream(
                        request.copy(
                            instructions = processToolCall(request, it.first()),
                        )
                    )
                } else {
                    return it
                }
            }

    }

    private fun isToolCall(chatResponse: ChatResponse): Boolean {
        return chatResponse.result.output.toolCall.isEmpty()
    }

    private fun processToolCall(prompt: Prompt, chatResponse: ChatResponse): List<Message> {
        return buildToolCallMessage(
            prompt.instructions,
            chatResponse.result.output,
            executeFunctionCall(getFunctionCallNames, chatResponse.result.output)
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