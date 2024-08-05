package io.kamo.ktor.client.ai.core.chat.client

import io.kamo.ktor.client.ai.core.chat.function.FunctionCall
import io.kamo.ktor.client.ai.core.chat.message.Message
import io.kamo.ktor.client.ai.core.chat.prompt.ChatOptions

data class ChatClientRequest(
    val chatOptions: ChatOptions,
    var systemText: Map<String, Any>.() -> String? = { null },
    var userText: Map<String, Any>.() -> String? = { null },
    val userParams: MutableMap<String, Any> = mutableMapOf(),
    val systemParams: MutableMap<String, Any> = mutableMapOf(),
    val functionCalls: MutableList<FunctionCall> = mutableListOf(),
    val functionNames: MutableSet<String> = mutableSetOf(),
    val enhancers: MutableList<Enhancer> = mutableListOf(),
    val enhanceParams: MutableMap<String, Any> = mutableMapOf(),
    val messages: MutableList<Message> = mutableListOf(),
) {
    constructor(clientRequest: ChatClientRequest) : this(
        chatOptions = clientRequest.chatOptions,
        systemText = clientRequest.systemText,
        userText = clientRequest.userText,
        functionNames = clientRequest.functionNames.toMutableSet(),
        functionCalls = clientRequest.functionCalls.toMutableList(),
        userParams = clientRequest.userParams.toMutableMap(),
        systemParams = clientRequest.systemParams.toMutableMap(),
        enhancers = clientRequest.enhancers.toMutableList(),
        enhanceParams = clientRequest.enhanceParams.toMutableMap(),
        messages = clientRequest.messages.toMutableList(),
    )
}