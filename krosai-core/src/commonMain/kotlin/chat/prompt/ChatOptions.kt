@file:Suppress("unused")

package io.github.krosai.client.ai.core.chat.prompt

import io.github.krosai.client.ai.core.model.ModelOptions

interface ChatOptions : ModelOptions {

    val temperature: Float?

    val topP: Float?

    val topK: Int?

    fun copy(): ChatOptions
}