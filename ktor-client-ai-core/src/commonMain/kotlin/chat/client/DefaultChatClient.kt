package io.kamo.ktor.client.ai.core.chat.client

import io.kamo.ktor.client.ai.core.chat.function.FunctionCall
import io.kamo.ktor.client.ai.core.chat.function.FunctionCallOptions
import io.kamo.ktor.client.ai.core.chat.message.Message
import io.kamo.ktor.client.ai.core.chat.model.ChatModel
import io.kamo.ktor.client.ai.core.chat.model.ChatResponse
import io.kamo.ktor.client.ai.core.chat.prompt.ChatOptions
import io.kamo.ktor.client.ai.core.chat.prompt.Prompt
import kotlinx.coroutines.flow.Flow


class DefaultChatClient(
    private val chatModel: ChatModel,
    private val defaultRequest: DefaultChatClientRequestScope,
) : ChatClient {

    override suspend fun call(request: ChatClientRequestScopeSpec): ChatResponse {
        val requestScope = DefaultChatClientRequestScope(defaultRequest)
            .also { request?.invoke(it) }

        if (requestScope.chatOptions is FunctionCallOptions) {
            requestScope.chatOptions.functionCalls.addAll(requestScope.functionCalls)
            requestScope.chatOptions.functionNames.addAll(requestScope.functionNames)
        }

        val prompt = creatPrompt(requestScope)
        return chatModel.call(prompt)
    }

    override suspend fun stream(request: ChatClientRequestScopeSpec): Flow<ChatResponse> {
        val requestScope = DefaultChatClientRequestScope(defaultRequest)
            .also { request?.invoke(it) }

        if (requestScope.chatOptions is FunctionCallOptions) {
            requestScope.chatOptions.functionCalls.addAll(requestScope.functionCalls)
            requestScope.chatOptions.functionNames.addAll(requestScope.functionNames)
        }

        val prompt = creatPrompt(requestScope)
        return chatModel.stream(prompt)
    }

    private fun creatPrompt(requestScope: DefaultChatClientRequestScope): Prompt {

        val messages: List<Message> = listOfNotNull(
            requestScope.userText.invoke(requestScope.userParams)?.let { Message.User(it) },
            requestScope.systemText.invoke(requestScope.systemParams)?.let { Message.System(it) },
        )
        val prompt = Prompt(
            instructions = messages,
            options = requestScope.chatOptions
        )
        return prompt
    }

}


class DefaultChatClientRequestScope(
    val model: ChatModel,
    val chatOptions: ChatOptions,
    override var systemText: Map<String, Any>.() -> String? = { null },
    override var userText: Map<String, Any>.() -> String? = { null },
    val functionNames: MutableSet<String> = mutableSetOf(),
    val functionCalls: MutableList<FunctionCall> = mutableListOf(),
    val userParams: MutableMap<String, Any> = mutableMapOf(),
    val systemParams: MutableMap<String, Any> = mutableMapOf()
) : ChatClientRequestScope {

    constructor(model: ChatModel, chatOption: ChatOptions?) : this(
        model = model,
        chatOptions = chatOption?.copy() ?: model.defaultChatOptions.copy(),
    )

    constructor(other: DefaultChatClientRequestScope) : this(
        userText = other.userText,
        systemText = other.systemText,
        model = other.model,
        chatOptions = other.chatOptions,
        functionNames = other.functionNames,
        functionCalls = other.functionCalls,
        userParams = LinkedHashMap(other.systemParams),
        systemParams = LinkedHashMap(other.systemParams)
    )

    override fun user(userScope: PromptUserScope.() -> Unit) {
        DefaultPromptUserScope(this).apply(userScope)
    }

    override fun system(systemScope: PromptSystemScope.() -> Unit) {
        DefaultPromptSystemScope(this).apply(systemScope)
    }

    override fun functions(functionCallScope: FunctionCallScope.() -> Unit) {
        DefaultFunctionCallScope(this).apply(functionCallScope)
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
    chatClientRequestScope: DefaultChatClientRequestScope
) : FunctionCallScope {

    override val functionCalls: MutableList<FunctionCall> = chatClientRequestScope.functionCalls

    override val functions: MutableSet<String> = mutableSetOf()

    override fun function(vararg functionNames: String) {
        functions.addAll(functionNames)
    }

}