package io.github.krosai.core.chat.memory

import io.github.krosai.core.chat.message.Message

/**
 * The MessageStore interface for represents a storage for chat conversation history.
 * Provides saving and retrieving messages in a conversation.
 * @author KAMOsama
 */
interface MessageStore {

    /**
     * Retrieves a list of messages associated with a conversation ID,
     * taking the specified number of last messages.
     *
     * @param conversationId the ID of the conversation
     * @param takeLastN the number of last messages to retrieve
     * @return a list of messages associated with the conversation ID
     */
    fun get(conversationId: String, takeLastN: Int): List<Message>

    /**
     * Adds a message to the conversation history.
     *
     * @param conversationId the ID of the conversation
     * @param message the message to be added
     */
    fun add(conversationId: String, message: Message)

    /**
     * Clears all the messages associated with the given conversation ID.
     *
     * @param conversationId the ID of the conversation for which the messages should be cleared
     */
    fun clear(conversationId: String)

}

/**
 * Returns the list of messages associated with a conversation pair.
 *
 * @param conversationPair the conversation pair representing the conversation ID and  the number of recent messages to be fetched
 * @return the list of messages associated with the conversation pair
 */
operator fun MessageStore.get(conversationPair: Pair<String, Int>): List<Message> =
    get(conversationPair.first, conversationPair.second)

/**
 * Adds a message pair to the message store.
 *
 * @param messagePair the pair containing the conversation ID and the message to be added
 */
operator fun MessageStore.plusAssign(messagePair: Pair<String, Message>) =
    add(messagePair.first, messagePair.second)