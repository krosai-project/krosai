package io.kamo.ktor.client.ai.core

import io.kamo.ktor.client.ai.core.message.Message
import model.ModelOptions
import model.ModelRequest

data class Prompt(
    override val instructions: List<Message> = listOf(),
    override var options: ModelOptions? = null,
): ModelRequest<List<Message>> {

    constructor(vararg messages: Message): this(messages.toList())

    constructor(contents: String): this(listOf(Message.User(contents)))


}
