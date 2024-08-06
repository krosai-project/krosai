@file:Suppress("unused")

package io.github.krosai.core.chat.prompt

import io.github.krosai.core.model.ModelOptions

interface ChatOptions : ModelOptions {

    val temperature: Float?

    val topP: Float?

    val topK: Int?

    fun copy(): ChatOptions

}