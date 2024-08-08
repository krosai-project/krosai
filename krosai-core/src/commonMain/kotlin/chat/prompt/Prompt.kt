@file:Suppress("unused")

package io.github.krosai.core.chat.prompt

import io.github.krosai.core.chat.message.Message
import io.github.krosai.core.model.ModelRequest

/**
 * A prompt for a chat model.
 *
 * @property instructions The instructions or input required by the AI model.
 * @property options The customizable options for AI model interactions.
 * @constructor Creates a new prompt with the specified instructions and options.
 * @author KAMOsama
 */
data class Prompt(
    override val instructions: List<Message> = listOf(),
    override var options: ChatOptions,
): ModelRequest<List<Message>> {

    constructor(vararg messages: Message, options: ChatOptions): this(messages.toList(), options)

    constructor(contents: String, options: ChatOptions): this(listOf(Message.User(contents)), options)

}