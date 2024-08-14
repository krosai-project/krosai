package org.krosai.openai.metadata

import org.krosai.core.chat.metadata.Usage

data class OpenAiUsage(
    val usage: org.krosai.openai.api.Usage
) : Usage {

    override val promptTokens: Long
        get() = usage.promptTokens?.toLong() ?: 0

    override val generationTokens: Long
        get() = usage.completionTokens?.toLong() ?: 0

    override val totalTokens: Long
        get() = usage.totalTokens?.toLong() ?: (promptTokens + generationTokens)

}