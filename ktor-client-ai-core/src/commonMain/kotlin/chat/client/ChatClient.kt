package io.kamo.ktor.client.ai.core.chat.client

import io.kamo.ktor.client.ai.core.chat.model.ChatResponse
import kotlinx.coroutines.flow.Flow

interface ChatClient {

    suspend fun call(request: ChatClientRequestScopeSpec): ChatResponse

    suspend fun stream(request: ChatClientRequestScopeSpec): Flow<ChatResponse>

}

typealias ChatClientRequestScopeSpec = (ChatClientRequestScope.() -> Unit)?

interface ChatClientRequestScope {

    var model: String

    var userText: Map<String, Any>.() -> String?

    var systemText: Map<String, Any>.() -> String?

    fun user(userScope: PromptUserScope.()->Unit)

    fun system(systemScope: PromptSystemScope.()->Unit)

}

interface PromptUserScope {

    var text: Map<String,Any>.() -> String?

    infix fun String.to(value: Any)

}

interface PromptSystemScope{

    var text: Map<String,Any>.() -> String?

    infix fun String.to(value: Any)

}

