package io.kamo.ktor.client.ai.core

import io.kamo.ktor.client.ai.core.model.ChatResponse
import io.ktor.client.*
import io.ktor.client.plugins.*
import kotlinx.coroutines.flow.Flow

suspend fun HttpClient.chat(content: String): ChatResponse {
    val aiPlugin = this.attributes[AiPluginKey]
    val aiPluginInstance = plugin(aiPlugin)
    return aiPluginInstance.chatModel.call(Prompt(content))
}

suspend fun HttpClient.steaming(content: String): Flow<ChatResponse> {
    val aiPlugin = this.attributes[AiPluginKey]
    val aiPluginInstance = plugin(aiPlugin)
    return aiPluginInstance.chatModel.stream(Prompt(content))
}


