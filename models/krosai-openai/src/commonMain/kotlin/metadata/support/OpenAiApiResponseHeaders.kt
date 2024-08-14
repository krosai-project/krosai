package org.krosai.openai.metadata.support


/**
 * Enum class representing the headers in the OpenAiApiResponse.
 *
 * @property header The header name.
 * @property description The description of the header.
 */
enum class OpenAiApiResponseHeaders(val header: String, val description: String) {

    REQUESTS_LIMIT_HEADER("x-ratelimit-limit-requests", "Total number of requests allowed within timeframe."),

    REQUESTS_REMAINING_HEADER("x-ratelimit-remaining-requests", "Remaining number of requests available in timeframe."),

    REQUESTS_RESET_HEADER("x-ratelimit-reset-requests", "Duration of time until the number of requests reset."),

    TOKENS_RESET_HEADER("x-ratelimit-reset-tokens", "Total number of tokens allowed within timeframe."),

    TOKENS_LIMIT_HEADER("x-ratelimit-limit-tokens", "Remaining number of tokens available in timeframe."),

    TOKENS_REMAINING_HEADER("x-ratelimit-remaining-tokens", "Duration of time until the number of tokens reset.");

}