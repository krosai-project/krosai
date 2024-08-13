package io.github.krosai.openai.metadata

import io.github.krosai.core.chat.metadata.RateLimit
import kotlin.time.Duration

data class OpenAiRateLimit(
    override val requestsLimit: Long,
    override val requestsRemaining: Long,
    override val requestsReset: Duration,
    override val tokensLimit: Long,
    override val tokensRemaining: Long,
    override val tokensReset: Duration
) : RateLimit