package io.kamo.ktor.client.ai.core.chat.message

import io.ktor.http.LinkHeader.Parameters.Media

enum class MessageType(val value: String) {

    SYSTEM("system"),

    USER("user"),

    ASSISTANT("assistant"),

    FUNCTION("function");

    fun fromValue(value: String): MessageType {
        for (messageType in MessageType.entries) {
            Media
            if (messageType.value == value) {
                return messageType
            }
        }
        throw IllegalArgumentException("Invalid MessageType value: $value")
    }
}