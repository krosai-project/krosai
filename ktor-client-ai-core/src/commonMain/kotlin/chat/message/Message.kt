@file:Suppress("unused")
package io.kamo.ktor.client.ai.core.chat.message

sealed class Message(
    val type: MessageType,
    val content: String,
){
    class System(content: String): Message(MessageType.SYSTEM, content)

    class User(content: String): Message(MessageType.USER, content)

    class Assistant(content: String,val properties: Map<String, Any>): Message(MessageType.ASSISTANT, content)

    class Function(content: String,val name: String,val arguments: String): Message(MessageType.FUNCTION, content)

}


