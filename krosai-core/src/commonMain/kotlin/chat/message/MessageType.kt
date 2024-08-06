package io.github.krosai.client.ai.core.chat.message

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