@file:Suppress("unused")

package org.krosai.core.chat.prompt

import org.krosai.core.model.ModelOptions

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