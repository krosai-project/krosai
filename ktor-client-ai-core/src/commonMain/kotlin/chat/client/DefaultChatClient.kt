package io.kamo.ktor.client.ai.core.chat.client

import io.kamo.ktor.client.ai.core.chat.function.FunctionCall
import io.kamo.ktor.client.ai.core.chat.message.Message
import io.kamo.ktor.client.ai.core.chat.model.ChatModel
import io.kamo.ktor.client.ai.core.chat.model.ChatResponse
import io.kamo.ktor.client.ai.core.chat.prompt.Prompt
import kotlinx.coroutines.flow.Flow


class DefaultChatClient(
    private val chatModel: ChatModel,
    private val defaultRequest: DefaultChatClientRequestScope = DefaultChatClientRequestScope(),
) : ChatClient {

    override suspend fun call(request: ChatClientRequestScopeSpec): ChatResponse {
        val prompt = creatPrompt(request)
        return chatModel.call(prompt)
    }

    override suspend fun stream(request: ChatClientRequestScopeSpec): Flow<ChatResponse> {
        val prompt = creatPrompt(request)
        return chatModel.stream(prompt)
    }

    private fun creatPrompt(request: ChatClientRequestScopeSpec): Prompt {
        val requestScope = DefaultChatClientRequestScope(defaultRequest)
            .also { request?.invoke(it) }
        val messages: List<Message> = listOfNotNull(
            requestScope.userText.invoke(requestScope.userParams)?.let { Message.User(it) },
            requestScope.systemText.invoke(requestScope.systemParams)?.let { Message.System(it) },
        )
        val prompt = Prompt(
            instructions = messages
        )
        return prompt
    }

}


class DefaultChatClientRequestScope(
    override var model: String = "",
    override var userText: Map<String, Any>.() -> String? = { null },
    override var systemText: Map<String, Any>.() -> String? = { null },
    val userParams: MutableMap<String, Any> = mutableMapOf(),
    val systemParams: MutableMap<String, Any> = mutableMapOf(),
    val functionCalls: MutableList<FunctionCall> = mutableListOf()
) : ChatClientRequestScope {

    constructor(other: DefaultChatClientRequestScope) : this(
        model = other.model,
        userText = other.userText,
        systemText = other.systemText,
        userParams = LinkedHashMap(other.systemParams),
        systemParams = LinkedHashMap(other.systemParams),
    )

    override fun user(userScope: PromptUserScope.() -> Unit) {
        DefaultPromptUserScope(this).apply(userScope)
    }

    override fun system(systemScope: PromptSystemScope.() -> Unit) {
        DefaultPromptSystemScope(this).apply(systemScope)
    }

    override fun functions(functionCallScope: FunctionCallScope.() -> Unit) {

    }


}

class DefaultPromptUserScope(
    private val chatClientRequestScope: DefaultChatClientRequestScope
) : PromptUserScope {

    override var text: Map<String, Any>.() -> String? by chatClientRequestScope::userText

    private val params: MutableMap<String, Any> by chatClientRequestScope::userParams

    override fun String.to(value: Any) {
        params[this] = value
    }

}

class DefaultPromptSystemScope(
    private val chatClientRequestScope: DefaultChatClientRequestScope
) : PromptSystemScope {

    override var text: Map<String, Any>.() -> String? by chatClientRequestScope::systemText

    private val params: MutableMap<String, Any> by chatClientRequestScope::systemParams

    override fun String.to(value: Any) {
        params[this] = value
    }

}

class DefaultFunctionCallScope(
) : FunctionCallScope {

    override  val functionCalls: MutableList<FunctionCall> = mutableListOf()


    override fun function(vararg functionNames: String) {
        TODO("Not yet implemented")
    }

}