package org.krosai.core.chat.message

/**
 * The type of a message.
 *
 * @property value The string value of the message type.
 *
 * @author KAMOsama
 */
enum class MessageType(val value: String) {

    SYSTEM("system"),

    USER("user"),

    ASSISTANT("assistant"),

    TOOL("tool");

    fun fromValue(value: String): MessageType {
        for (messageType in MessageType.entries) {
            if (messageType.value == value) {
                return messageType
            }
        }
        throw IllegalArgumentException("Invalid MessageType value: $value")
    }
}