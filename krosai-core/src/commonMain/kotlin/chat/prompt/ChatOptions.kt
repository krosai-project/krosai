@file:Suppress("unused")

package io.github.krosai.core.chat.prompt

import io.github.krosai.core.model.ModelOptions

/**
 * This interface represents the options for a chat model.
 *
 * @author KAMOsama
 */
interface ChatOptions : ModelOptions {

    val temperature: Float?

    val topP: Float?

    val topK: Int?

    fun copy(): ChatOptions

}