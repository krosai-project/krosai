package io.github.krosai.core.chat.metadata

import kotlin.time.Duration

interface RateLimit {

    /**
     * Returns the maximum number of requests that are permitted before exhausting the
     * rate limit.
     * @return an [Long] with the maximum number of requests that are permitted
     * before exhausting the rate limit.
     * @see .RequestsRemaining
     */
    val requestsLimit: Long

    /**
     * Returns the remaining number of requests that are permitted before exhausting the
     * [rate limit][.RequestsLimit].
     * @return an [Long] with the remaining number of requests that are permitted
     * before exhausting the [rate limit][.RequestsLimit].
     * @see .RequestsLimit
     */
    val requestsRemaining: Long

    /**
     * Returns the [time][Duration] until the rate limit (based on requests) resets
     * to its [initial state][.RequestsLimit].
     * @return a [Duration] representing the time until the rate limit (based on
     * requests) resets to its [initial state][.RequestsLimit].
     * @see .RequestsLimit
     */
    val requestsReset: Duration

    /**
     * Returns the maximum number of tokens that are permitted before exhausting the rate
     * limit.
     * @return an [Long] with the maximum number of tokens that are permitted before
     * exhausting the rate limit.
     * @see .TokensRemaining
     */
    val tokensLimit: Long

    /**
     * Returns the remaining number of tokens that are permitted before exhausting the
     * [rate limit][.TokensLimit].
     * @return an [Long] with the remaining number of tokens that are permitted
     * before exhausting the [rate limit][.TokensLimit].
     * @see .TokensLimit
     */
    val tokensRemaining: Long

    /**
     * Returns the [time][Duration] until the rate limit (based on tokens) resets to
     * its [initial state][.TokensLimit].
     * @return a [Duration] with the time until the rate limit (based on tokens)
     * resets to its [initial state][.TokensLimit].
     * @see .TokensLimit
     */
    val tokensReset: Duration

    /**
     * An empty implementation of [RateLimit].
     */
    companion object {
        val EMPTY = object : RateLimit {
            override val requestsLimit: Long = 0L
            override val requestsRemaining: Long = 0L
            override val requestsReset: Duration = Duration.ZERO
            override val tokensLimit: Long = 0L
            override val tokensRemaining: Long = 0L
            override val tokensReset: Duration = Duration.ZERO
        }
    }

}

