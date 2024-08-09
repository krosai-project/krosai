package io.github.krosai.openai.metadata

import io.github.krosai.openai.api.Usage

data class OpenAiUsage(
    val usage: Usage
): io.github.krosai.core.chat.metadata.Usage {

    override val promptTokens: Long?
        get() = usage.promptTokens?.toLong() ?: 0

    override val generationTokens: Long?
        get() = usage.completionTokens?.toLong() ?: 0

    override val totalTokens: Long
        get() = usage.totalTokens?.toLong() ?: 0
}