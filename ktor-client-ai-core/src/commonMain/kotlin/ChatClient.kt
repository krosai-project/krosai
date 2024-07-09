package io.kamo.ktor.client.ai.core

import io.kamo.ktor.client.ai.core.model.ChatResponse
import kotlinx.coroutines.flow.Flow

interface ChatClient {


    fun request(request: ChatClientRequestScope.() -> Unit): ChatClientRequestScope

}

interface ChatClientRequestScope {

    var model: String

    var userText: String

    var systemText: String

    fun user(userScope: PromptUserScope.()->Unit)

    fun system(systemScope: PromptSystemScope.()->Unit)

   suspend fun call():ChatResponse

    suspend fun stream(): Flow<ChatResponse>
}

interface PromptUserScope {

    var text: String?

    val params: Map<String,String>

    fun params(vararg params: Pair<String,String>)
}

interface PromptSystemScope{

    var text: String?

    val params: Map<String,String>

    fun params(vararg params: Pair<String,String>)
}