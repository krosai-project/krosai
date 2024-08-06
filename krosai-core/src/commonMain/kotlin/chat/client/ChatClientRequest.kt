package io.github.krosai.core.chat.client

import io.github.krosai.core.chat.enhancer.Enhancer
import io.github.krosai.core.chat.function.FunctionCall
import io.github.krosai.core.chat.message.Message
import io.github.krosai.core.chat.prompt.ChatOptions

data class ChatClientRequest(
    val chatOptions: ChatOptions,
    var systemText: Map<String, Any>.() -> String? = { null },
    var userText: Map<String, Any>.() -> String? = { null },
    val userParams: MutableMap<String, Any> = mutableMapOf(),
    val systemParams: MutableMap<String, Any> = mutableMapOf(),
    val functionCalls: MutableList<FunctionCall> = mutableListOf(),
    val functionNames: MutableSet<String> = mutableSetOf(),
    val enhancers: MutableList<Enhancer> = mutableListOf(),
    val enhancerParams: MutableMap<String, Any> = mutableMapOf(),
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
        enhancerParams = clientRequest.enhancerParams.toMutableMap(),
        messages = clientRequest.messages.toMutableList(),
    )
}