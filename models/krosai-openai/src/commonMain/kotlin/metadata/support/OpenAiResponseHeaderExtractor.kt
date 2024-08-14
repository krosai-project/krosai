package org.krosai.openai.metadata.support

import io.ktor.client.statement.*
import org.krosai.core.chat.metadata.RateLimit
import org.krosai.openai.metadata.OpenAiRateLimit
import org.krosai.openai.metadata.support.OpenAiApiResponseHeaders.*
import kotlin.time.Duration

internal fun HttpResponse.extractAiResponseHeaders(): RateLimit {

    val requestsLimit: Long = getHeaderAsLong(REQUESTS_LIMIT_HEADER.header)

    val requestsRemaining: Long = getHeaderAsLong(REQUESTS_REMAINING_HEADER.header)

    val tokensLimit: Long = getHeaderAsLong(TOKENS_LIMIT_HEADER.header)

    val tokensRemaining: Long = getHeaderAsLong(TOKENS_REMAINING_HEADER.header)

    val requestsReset: Duration = getHeaderAsDuration(REQUESTS_RESET_HEADER.header)

    val tokensReset: Duration = getHeaderAsDuration(TOKENS_RESET_HEADER.header)

    return OpenAiRateLimit(
        requestsLimit = requestsLimit,
        requestsRemaining = requestsRemaining,
        tokensLimit = tokensLimit,
        tokensRemaining = tokensRemaining,
        requestsReset = requestsReset,
        tokensReset = tokensReset
    )
}

private fun HttpResponse.getHeaderAsDuration(header: String): Duration =
    this.headers[header]?.let {
        Duration.parseOrNull(it)
    } ?: Duration.ZERO


private fun HttpResponse.getHeaderAsLong(header: String): Long =
    this.headers[header]?.toLong() ?: 0

