package io.kamo.ktor.client.ai.openai.api

import io.kamo.ktor.client.ai.core.Prompt
import io.kamo.ktor.client.ai.openai.config.OpenAiOptions
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ChatCompletionRequest(
    @SerialName("messages") val messages: List<ChatCompletionMessage>,
    @SerialName("model") val model: String,
    @SerialName("frequency_penalty") val frequencyPenalty: Float? = null,
    @SerialName("logit_bias") val logitBias: Map<String, Int>? = null,
    @SerialName("logprobs") val logprobs: Boolean? = null,
    @SerialName("top_logprobs") val topLogprobs: Int? = null,
    @SerialName("max_tokens") val maxTokens: Int? = null,
    @SerialName("n") val n: Int? = null,
    @SerialName("presence_penalty") val presencePenalty: Float? = null,
    @SerialName("response_format") val responseFormat: ResponseFormat? = null,
    @SerialName("seed") val seed: Int? = null,
    @SerialName("stop") val stop: List<String>? = null,
    @SerialName("stream") val stream: Boolean?,
    @SerialName("stream_options") val streamOptions: StreamOptions? = null,
    @SerialName("temperature") val temperature: Float? = null,
    @SerialName("top_p") val topP: Float? = null,
    @SerialName("tools") val tools: List<FunctionTool>? = null,
//    @SerialName("tool_choice") val   toolChoice:Any = null,
    @SerialName("user") val user: String? = null
) {
    @Serializable
    data class ChatCompletionMessage(
//    @SerialName("content") val rawContent: Any,
        @SerialName("role") val role: Role,
        @SerialName("name") val name: String,
        @SerialName("tool_call_id") val toolCallId: String? = null,
        @SerialName("tool_calls") val toolCalls: List<ToolCall>? = null,
    ) {
        enum class Role {
            /**
             * System message.
             */
            @SerialName("system")
            SYSTEM,

            /**
             * User message.
             */
            @SerialName("user")
            USER,

            /**
             * Assistant message.
             */
            @SerialName("assistant")
            ASSISTANT,

            /**
             * Tool message.
             */
            @SerialName("tool")
            TOOL;

        }

        @Serializable
        data class ToolCall(
            @SerialName("id") val id: String,
            @SerialName("type") val type: String,
            @SerialName("function") val function: ChatCompletionFunction
        )

        @Serializable
        data class ChatCompletionFunction(
            @SerialName("name") val name: String,
            @SerialName("arguments") val arguments: String
        )

    }

    @Serializable
    data class ResponseFormat(@SerialName("type") val type: String)

    @Serializable
    data class StreamOptions(@SerialName("include_usage") val includeUsage: Boolean) {
        companion object {
            var INCLUDE_USAGE: StreamOptions = StreamOptions(true)
        }
    }

    @Serializable
    data class FunctionTool(
        @SerialName("type") val  type: Type,
        @SerialName("function")val   function:Function
    ){

        @Serializable
        enum class Type {
            /**
             * Function tool type.
             */
            @SerialName("function")
            FUNCTION
        }

        @Serializable
        data class Function(
            @SerialName("description") val description: String,
            @SerialName("name") val name: String,
            @SerialName("parameters") val parameters: Map<String, String>
        ) {
            /**
             * Create tool function definition.
             *
             * @param description tool function description.
             * @param name tool function name.
             * @param jsonSchema tool function schema as json.
             */
            constructor(description: String, name: String, jsonSchema: String) : this(
                description,
                name,
                // TODO: parse jsonSchema
                emptyMap()
            )
        }
    }

    companion object {
        fun build(options: OpenAiOptions, prompt: Prompt, stream:Boolean): ChatCompletionRequest {
            val chatCompletionMessages = prompt.messages.map {
                ChatCompletionMessage(
                    ChatCompletionMessage.Role.valueOf(it.type.value.uppercase()),
                    it.content
                )
            }
            return ChatCompletionRequest(chatCompletionMessages,options.model, stream = stream)
        }
    }
}

