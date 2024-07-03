package io.kamo.ktor.client.ai.core

import io.ktor.client.*
import io.ktor.client.plugins.*

suspend fun HttpClient.chat(content: String): String {
    val aiPlugin = this.attributes[AiPluginKey]
    val aiPluginInstance = plugin(aiPlugin)
    return aiPluginInstance.chatModel.call(Prompt(content))
}


