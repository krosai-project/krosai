@file:Suppress("unused")
package io.kamo.ktor.client.ai.core.chat.prompt

import io.kamo.ktor.client.ai.core.model.ModelOptions

interface ChatOptions : ModelOptions {

    val temperature: Float?

    val topP: Float?

    val topK: Int?

    fun copy(): ChatOptions
}