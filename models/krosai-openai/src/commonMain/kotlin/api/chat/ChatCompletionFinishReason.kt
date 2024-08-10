package io.github.krosai.openai.api.chat

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * The reason the model stopped generating tokens.
 */
@Serializable
enum class ChatCompletionFinishReason {
    /**
     * The model hit a natural stop point or a provided stop sequence.
     */
    @SerialName("stop")
    STOP,

    /**
     * The maximum number of tokens specified in the request was reached.
     */
    @SerialName("length")
    LENGTH,

    /**
     * The content was omitted due to a flag from our content filters.
     */
    @SerialName("content_filter")
    CONTENT_FILTER,

    /**
     * The model called a tool.
     */
    @SerialName("tool_calls")
    TOOL_CALLS,

    /**
     * The model called a function.([Deprecated])
     */
    @SerialName("function_call")
    FUNCTION_CALL,

    /**
     * Only for compatibility with Mistral AI API.
     */
    @SerialName("tool_call")
    TOOL_CALL
}