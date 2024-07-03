package io.kamo.ktor.client.ai.core

import io.kamo.ktor.client.ai.core.message.Message
import io.kamo.ktor.client.ai.core.message.MessageType

data class Prompt(
    val messages: List<Message> = listOf()
){
    constructor(vararg messages: Message): this(messages.toList())
    constructor(contents: String): this(listOf(Message(MessageType.USER,contents)))
}
