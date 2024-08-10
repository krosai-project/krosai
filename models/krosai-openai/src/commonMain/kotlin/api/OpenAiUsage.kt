package io.github.krosai.openai.api

import io.github.krosai.core.chat.metadata.Usage
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Usage statistics for the completion request.
 *
 * @param completionTokens Number of tokens in the generated completion. Only applicable for completion requests.
 * @param promptTokens Number of tokens in the prompt.
 * @param totalTokens Total number of tokens used in the request (prompt + completion).
 */
@Serializable
data class OpenAiUsage(
    @SerialName("completion_tokens") val completionTokens: Long?,
    @SerialName("prompt_tokens") override val promptTokens: Long?,
    @SerialName("total_tokens") override val totalTokens: Long
) : Usage {
    override val generationTokens: Long?
        get() = completionTokens
}