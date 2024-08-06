@file:Suppress("unused")

package io.github.krosai.client.ai.core.chat.prompt

import io.github.krosai.client.ai.core.chat.message.Message
import io.github.krosai.client.ai.core.model.ModelRequest

data class Prompt(
    override val instructions: List<Message> = listOf(),
    override var options: ChatOptions,
): ModelRequest<List<Message>> {

    constructor(vararg messages: Message, options: ChatOptions): this(messages.toList(), options)

    constructor(contents: String, options: ChatOptions): this(listOf(Message.User(contents)), options)

}