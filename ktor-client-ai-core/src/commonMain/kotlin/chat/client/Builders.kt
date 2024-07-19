package io.kamo.ktor.client.ai.core.chat.client

import io.kamo.ktor.client.ai.core.chat.model.ChatModel
import io.kamo.ktor.client.ai.core.plugin.aiPlugin
import io.ktor.client.*


fun HttpClient.createChatClient(
    creator: (ChatModel,ChatClientRequestScopeSpec) -> ChatClient = ::createDefaultChatClient,
    request: ChatClientRequestScopeSpec = null
): ChatClient {
    val aiPluginInstance = aiPlugin<Any>()
    return creator(aiPluginInstance.chatModel, request)
}

private fun createDefaultChatClient(chatModel: ChatModel, request: ChatClientRequestScopeSpec): ChatClient =
    DefaultChatClient(
        chatModel = chatModel,
        defaultRequest = DefaultChatClientRequestScope().apply { request?.invoke(this) }
    )


