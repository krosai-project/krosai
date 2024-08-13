package io.github.krosai.openai.model

import io.github.krosai.core.chat.function.FunctionCall
import io.github.krosai.core.chat.function.FunctionCallOptions
import io.github.krosai.core.chat.function.ToolCallHandler
import io.github.krosai.core.chat.message.Media
import io.github.krosai.core.chat.message.Message
import io.github.krosai.core.chat.message.MessageType
import io.github.krosai.core.chat.message.ToolCall
import io.github.krosai.core.chat.metadata.ChatGenerationMetadata
import io.github.krosai.core.chat.metadata.ChatResponseMetadata
import io.github.krosai.core.chat.metadata.RateLimit
import io.github.krosai.core.chat.metadata.Usage
import io.github.krosai.core.chat.model.ChatModel
import io.github.krosai.core.chat.model.ChatResponse
import io.github.krosai.core.chat.model.Generation
import io.github.krosai.core.chat.prompt.ChatOptions
import io.github.krosai.core.chat.prompt.Prompt
import io.github.krosai.openai.api.OpenAiApi
import io.github.krosai.openai.api.chat.ChatCompletion
import io.github.krosai.openai.api.chat.ChatCompletionChunk
import io.github.krosai.openai.api.chat.ChatCompletionMessage
import io.github.krosai.openai.api.chat.ChatCompletionRequest
import io.github.krosai.openai.api.chat.ChatCompletionRequest.FunctionTool
import io.github.krosai.openai.metadata.OpenAiUsage
import io.github.krosai.openai.metadata.support.extractAiResponseHeaders
import io.github.krosai.openai.options.OpenAiChatOptions
import io.ktor.client.call.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

/** OpenAI Chat API implementation. */


class OpenAiChatModel(
    private val api: OpenAiApi,
    private val chatOptions: OpenAiChatOptions,
    private val getFunctionCallNames: (Set<String>) -> List<FunctionCall>
) : ToolCallHandler, ChatModel {

    override val defaultChatOptions: ChatOptions get() = chatOptions.copy()

    override suspend fun call(prompt: Prompt): ChatResponse {
        val request = createRequest(prompt, false, getFunctionCallNames)
        return api.call(request)
            .toChatResponse()
            .let { chatResponse ->
                if (chatResponse.isToolCall()) {
                    val currentPrompt = prompt.copy(instructions = processToolCall(prompt, chatResponse))
                    call(currentPrompt)
                } else {
                    chatResponse
                }
            }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override suspend fun stream(prompt: Prompt): Flow<ChatResponse> {
        val request = createRequest(prompt, true, getFunctionCallNames)
        return api.stream(request)
            .map {
                it.toChatCompletion()
                    .toChatResponse()
            }
            .flatMapConcat { chatResponse ->
                return@flatMapConcat if (chatResponse.isToolCall()) {
                    val currentPrompt = prompt.copy(instructions = processToolCall(prompt, chatResponse))
                    stream(currentPrompt)
                } else {
                    flowOf(chatResponse)
                }
            }
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

    private fun ChatResponse.isToolCall(): Boolean {
        return this.result.output.toolCall?.isNotEmpty() ?: false
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
suspend fun HttpResponse.toChatResponse(): ChatResponse {
    val chatCompletion: ChatCompletion = body()
    val rateLimit = extractAiResponseHeaders()
    return chatCompletion.toChatResponse(rateLimit)
}

fun ChatCompletion.toChatResponse(rateLimit: RateLimit = RateLimit.Empty): ChatResponse {
    val generations = choices.map(::buildGeneration)
    val chatResponseMetadata = ChatResponseMetadata(
        id = id,
        model = model,
        rateLimit = rateLimit,
        usage = usage?.let(::OpenAiUsage) ?: Usage.Empty,
        metadata = hashMapOf(
            "created" to created,
            "system_fingerprint" to systemFingerprint.orEmpty()
        )
    )
    return ChatResponse(
        generations,
        chatResponseMetadata
    )
}

private fun ChatCompletion.buildGeneration(choice: ChatCompletion.Choice): Generation {
    val message = choice.message
    val assistant = Message.Assistant(
        content = message.content.orEmpty(),
        properties = mapOf(
            "id" to this.id,
            "role" to message.role.name,
            "index" to choice.index,
            "finish_reason" to (choice.finishReason?.name).orEmpty(),
            "refusal" to message.refusal.orEmpty(),
        ),
        toolCall = message.toolCalls?.map { toolCall ->
            ToolCall(
                toolCall.id.orEmpty(),
                toolCall.function.name.orEmpty(),
                "function",
                toolCall.function.arguments
            )
        }
    )
    val finishReason = choice.finishReason?.name.orEmpty()
    val chatGenerationMetadata = ChatGenerationMetadata.from(finishReason, null)
    return Generation(
        assistant, chatGenerationMetadata
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
                val chatCompletionMessage = if (message is Message.User && message.media.isNotEmpty()) {
                    ChatCompletionMessage(
                        extractMediaContent(message),
                        ChatCompletionMessage.Role.fromMessageType(message.type)
                    )
                } else {
                    ChatCompletionMessage(
                        message.content,
                        ChatCompletionMessage.Role.fromMessageType(message.type)
                    )
                }
                listOf(chatCompletionMessage)
            }

            MessageType.ASSISTANT -> {
                val assistantMessage = message as Message.Assistant
                val toolCalls = assistantMessage.toolCall?.map {
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

    // TODO: refactor
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

@OptIn(ExperimentalEncodingApi::class)
private fun extractMediaContent(message: Message.User): List<ChatCompletionMessage.MediaContent> {
    val mediaContent = ChatCompletionMessage.MediaContent(message.content)
    return listOf(mediaContent) + message.media.mapNotNull { media ->
        media.takeIf { it.contentType.match(ContentType.Image.Any) }
            ?.let {
                val content = when (it) {
                    is Media.Url -> it.content.toString()
                    is Media.Bytes -> "data:${it.contentType};base64,${Base64.encode(it.content)}"
                }
                ChatCompletionMessage.MediaContent(
                    ChatCompletionMessage.MediaContent.ImageUrl(content)
                )
            }
    }
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