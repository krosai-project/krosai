package org.krosai.openai.api

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Log probability information for the choice.
 *
 * @param content A list of message content tokens with log probability information.
 */
@Serializable
data class LogProbs(
    @SerialName("content") val content: List<Content>
) {
    /**
     * Message content tokens with log probability information.
     *
     * @param token The token.
     * @param logprob The log probability of the token.
     * @param probBytes A list of integers representing the UTF-8 bytes representation
     * of the token. Useful in instances where characters are represented by multiple
     * tokens and their byte representations must be combined to generate the correct
     * text representation. Can be null if there is no bytes representation for the token.
     * @param topLogprobs List of the most likely tokens and their log probability,
     * at this token position. In rare cases, there may be fewer than the number of
     * requested top_logprobs returned.
     */
    @Serializable
    data class Content(
        @SerialName("token") val token: String,
        @SerialName("logprob") val logprob: Float,
        @SerialName("bytes") val probBytes: List<Int>,
        @SerialName("top_logprobs") val topLogprobs: List<TopLogProbs>
    ) {
        /**
         * The most likely tokens and their log probability, at this token position.
         *
         * @param token The token.
         * @param logprob The log probability of the token.
         * @param probBytes A list of integers representing the UTF-8 bytes representation
         * of the token. Useful in instances where characters are represented by multiple
         * tokens and their byte representations must be combined to generate the correct
         * text representation. Can be null if there is no bytes representation for the token.
         */
        @Serializable
        data class TopLogProbs(
            @SerialName("token") val token: String,
            @SerialName("logprob") val logprob: Float,
            @SerialName("bytes") val probBytes: List<Int>
        )
    }
}