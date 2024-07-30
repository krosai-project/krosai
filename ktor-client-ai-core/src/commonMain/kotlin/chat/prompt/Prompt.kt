@file:Suppress("unused")
package io.kamo.ktor.client.ai.core.chat.prompt

import io.kamo.ktor.client.ai.core.chat.message.Message
import io.kamo.ktor.client.ai.core.model.ModelOptions
import io.kamo.ktor.client.ai.core.model.ModelRequest

data class Prompt(
    override val instructions: List<Message> = listOf(),
    override var options: ChatOptions?,
): ModelRequest<List<Message>> {

    constructor(vararg messages: Message, options: ChatOptions): this(messages.toList(), options)

    constructor(contents: String, options: ChatOptions): this(listOf(Message.User(contents)), options)

}