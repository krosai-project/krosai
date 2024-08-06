package io.github.krosai.client.ai.core.chat.memory

import io.github.krosai.client.ai.core.chat.message.Message

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