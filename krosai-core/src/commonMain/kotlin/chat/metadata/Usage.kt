package io.github.krosai.core.chat.metadata

interface Usage {
    val promptTokens: Long?
    val generationTokens: Long?
    val totalTokens: Long get() = (promptTokens ?: 0) + (generationTokens ?: 0)
}
