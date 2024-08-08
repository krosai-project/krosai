package io.github.krosai.core.chat.memory

import io.github.krosai.core.chat.message.Message

/**
 * InMemoryMessageStore is a simple implementation of the MessageStore interface.
 * It stores messages in a map, where the key is the conversation ID.
 * It is not thread-safe and should not be used in a production environment.
 * It is intended for testing purposes only.
 *
 * @author KAMOsama
 */
class InMemoryMessageStore : MessageStore {

    private val memoryMessages: MutableMap<String, MutableList<Message>> = mutableMapOf()


    override fun get(conversationId: String, takeLastN: Int): List<Message> {
        return memoryMessages[conversationId]?.takeLast(takeLastN) ?: emptyList()
    }


    override fun add(conversationId: String, message: Message) {
        memoryMessages.getOrPut(conversationId) { mutableListOf() }.add(message)
    }

    override fun clear(conversationId: String) {
        memoryMessages.remove(conversationId)
    }

}