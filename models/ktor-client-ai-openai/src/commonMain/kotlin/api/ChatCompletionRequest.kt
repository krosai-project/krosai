@file:Suppress("unused")

package io.kamo.ktor.client.ai.openai.api

import io.kamo.ktor.client.ai.core.chat.function.FunctionCall
import io.kamo.ktor.client.ai.core.chat.message.Message
import io.kamo.ktor.client.ai.core.chat.message.MessageType
import io.kamo.ktor.client.ai.core.chat.prompt.Prompt
import io.kamo.ktor.client.ai.openai.api.ChatCompletionRequest.ToolChoiceBuilder
import io.kamo.ktor.client.ai.openai.model.OpenAiChatModel
import io.kamo.ktor.client.ai.openai.options.OpenAiChatOptions
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

/**
 * Creates a model response for the given chat conversation.
 *
 * @param messages A list of messages comprising the conversation so far.
 * @param model ID of the model to use.
 * @param frequencyPenalty Number between -2.0 and 2.0. Positive values penalize new tokens based on their existing
 * frequency in the text so far, decreasing the model's likelihood to repeat the same line verbatim.
 * @param logitBias Modify the likelihood of specified tokens appearing in the completion. Accepts a JSON object
 * that maps tokens (specified by their token ID in the tokenizer) to an associated bias value from -100 to 100.
 * Mathematically, the bias is added to the logits generated by the model prior to sampling. The exact effect will
 * vary per model, but values between -1 and 1 should decrease or increase likelihood of selection; values like -100
 * or 100 should result in a ban or exclusive selection of the relevant token.
 * @param logprobs Whether to return log probabilities of the output tokens or not. If true, returns the log
 * probabilities of each output token returned in the 'content' of 'message'.
 * @param topLogprobs An integer between 0 and 5 specifying the number of most likely tokens to return at each token
 * position, each with an associated log probability. 'logprobs' must be set to 'true' if this parameter is used.
 * @param maxTokens The maximum number of tokens to generate in the chat completion. The total length of input
 * tokens and generated tokens is limited by the model's context length.
 * @param n How many chat completion choices to generate for each input message. Note that you will be charged based
 * on the number of generated tokens across all the choices. Keep n as 1 to minimize costs.
 * @param presencePenalty Number between -2.0 and 2.0. Positive values penalize new tokens based on whether they
 * appear in the text so far, increasing the model's likelihood to talk about new topics.
 * @param responseFormat An object specifying the format that the model must output. Setting to { "type":
 * "json_object" } enables JSON mode, which guarantees the message the model generates is valid JSON.
 * @param seed This feature is in Beta. If specified, our system will make a best effort to sample
 * deterministically, such that repeated requests with the same seed and parameters should return the same result.
 * Determinism is not guaranteed, and you should refer to the system_fingerprint response parameter to monitor
 * changes in the backend.
 * @param stop Up to 4 sequences where the API will stop generating further tokens.
 * @param stream If set, partial message deltas will be sent.Tokens will be sent as data-only server-sent events as
 * they become available, with the stream terminated by a data: DONE message.
 * @param streamOptions Options for streaming response. Only set this when you set.
 * @param temperature What sampling temperature to use, between 0 and 1. Higher values like 0.8 will make the output
 * more random, while lower values like 0.2 will make it more focused and deterministic. We generally recommend
 * altering this or top_p but not both.
 * @param topP An alternative to sampling with temperature, called nucleus sampling, where the model considers the
 * results of the tokens with top_p probability mass. So 0.1 means only the tokens comprising the top 10%
 * probability mass are considered. We generally recommend altering this or temperature but not both.
 * @param tools A list of tools the model may call. Currently, only functions are supported as a tool. Use this to
 * provide a list of functions the model may generate JSON inputs for.
 * @param toolChoice Controls which (if any) function is called by the model. none means the model will not call a
 * function and instead generates a message. auto means the model can pick between generating a message or calling a
 * function. Specifying a particular function via {"type: "function", "function": {"name": "my_function"}} forces
 * the model to call that function. none is the default when no functions are present. auto is the default if
 * functions are present. Use the [ToolChoiceBuilder] to create the tool choice value.
 * @param user A unique identifier representing your end-user, which can help OpenAI to monitor and detect abuse.
 *
 */
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
    @SerialName("tool_choice") val toolChoice: String? = null,
    @SerialName("user") val user: String? = null
) {
    /**
     * Helper factory that creates a tool_choice of type 'none', 'auto' or selected function by name.
     */
    object ToolChoiceBuilder {

        /**
         * Model can pick between generating a message or calling a function.
         */
        var Auto: String = "auto"

        /**
         * Model will not call a function and instead generates a message
         */
        var None: String = "none"

        /**
         * Specifying a particular function forces the model to call that function.
         */
        fun function(name: String): String =
            "{\"type\": \"function\", \"function\": {\"name\": \"$name\"}}"
    }

    /**
     * An object specifying the format that the model must output.
     * @param type Must be one of 'text' or 'json_object'.
     */
    @Serializable
    data class ResponseFormat(@SerialName("type") val type: String)

    /**
     * @param includeUsage If set, an additional chunk will be streamed
     * before the data: DONE message. The usage field on this chunk
     * shows the token usage statistics for the entire request, and
     * the choices field will always be an empty array. All other chunks
     * will also include a usage field, but with a null value.
     */
    @Serializable
    data class StreamOptions(@SerialName("include_usage") val includeUsage: Boolean) {
        companion object {
            var IncludeUsage: StreamOptions = StreamOptions(true)
        }
    }

    /**
     * Represents a tool the model may call. Currently, only functions are supported as a tool.
     *
     * @param type The type of the tool. Currently, only 'function' is supported.
     * @param function The function definition.
     */
    @Serializable
    data class FunctionTool(
        @SerialName("type") val type: Type,
        @SerialName("function") val function: Function
    ) {
        constructor(function: Function) : this(Type.FUNCTION, function)

        /**
         * Create a tool of type 'function' and the given function definition.
         */
        @Serializable
        enum class Type {
            /**
             * Function tool type.
             */
            @SerialName("function")
            FUNCTION
        }

        /**
         * Function definition.
         *
         * @param description A description of what the function does, used by the model to choose when and how to call
         * the function.
         * @param name The name of the function to be called. Must be a-z, A-Z, 0-9, or contain underscores and dashes,
         * with a maximum length of 64.
         * @param parameters The parameters the functions accepts, described as a JSON Schema object. To describe a
         * function that accepts no parameters, provide the value {"type": "object", "properties": {}}.
         */
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
                Json.decodeFromString<Map<String, String>>(jsonSchema)
            )
        }
    }

    companion object {
        fun build(
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

            println("CCR: $toolCalls")

            return ChatCompletionRequest(
                chatCompletionMessages,
                chatOptions.model,
                stream = stream,
                tools = toolCalls,
            )
        }
    }
}

