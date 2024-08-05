package io.kamo.ktor.client.ai.core.chat.client

import io.kamo.ktor.client.ai.core.chat.function.FunctionCall
import io.kamo.ktor.client.ai.core.chat.function.FunctionCallOptions
import io.kamo.ktor.client.ai.core.chat.message.Message
import io.kamo.ktor.client.ai.core.chat.model.ChatModel
import io.kamo.ktor.client.ai.core.chat.model.ChatResponse
import io.kamo.ktor.client.ai.core.chat.prompt.ChatOptions
import io.kamo.ktor.client.ai.core.chat.prompt.Prompt
import kotlinx.coroutines.flow.Flow
import kotlin.reflect.KFunction


class DefaultChatClient(
    private val chatModel: ChatModel,
    private val defaultRequest: DefaultChatClientRequestScope,
) : ChatClient {

    override suspend fun call(requestScopeSpec: ChatClientRequestScopeSpec): ChatResponse {
        val requestScope = DefaultChatClientRequestScope(defaultRequest)
            .also { requestScopeSpec?.invoke(it) }

        var request = requestScope.chatClientRequest
        val enhancers = request.enhancers
        request = enhanceProcessor(
            enhancers,
            request,
            Enhancer::enhanceRequest
        )

        val prompt = creatPrompt(request)

        var response = chatModel.call(prompt)

        response = enhanceProcessor(
            enhancers,
            response,
            Enhancer::enhanceResponse
        )
        return response
    }

    override suspend fun stream(requestScopeSpec: ChatClientRequestScopeSpec): Flow<ChatResponse> {
        val requestScope = DefaultChatClientRequestScope(defaultRequest)
            .also { requestScopeSpec?.invoke(it) }

        var request = requestScope.chatClientRequest
        val enhancers = request.enhancers
        request = enhanceProcessor(
            enhancers,
            request,
            Enhancer::enhanceRequest
        )

        val prompt = creatPrompt(request)

        val responseFlow = chatModel.stream(prompt)
        return enhanceProcessor(
            enhancers,
            responseFlow,
            Enhancer::enhanceResponse
        )
    }


    private fun creatPrompt(request: ChatClientRequest): Prompt {

        val messages: List<Message> = request.messages + listOfNotNull(
            request.userText.invoke(request.userParams)?.let { Message.User(it) },
            request.systemText.invoke(request.systemParams)?.let { Message.System(it) },
        )
        if (request.chatOptions is FunctionCallOptions) {
            request.chatOptions.functionCalls.addAll(request.functionCalls)
            request.chatOptions.functionNames.addAll(request.functionNames)
        }
        val prompt = Prompt(
            instructions = messages,
            options = request.chatOptions
        )
        return prompt
    }

}


class DefaultChatClientRequestScope(
    val model: ChatModel,
    val chatClientRequest: ChatClientRequest
) : ChatClientRequestScope {

    constructor(model: ChatModel, chatOptions: ChatOptions?) : this(
        model = model,
        chatClientRequest = ChatClientRequest(
            chatOptions = chatOptions?.copy() ?: model.defaultChatOptions.copy()
        ),
    )

    constructor(other: DefaultChatClientRequestScope) : this(
        model = other.model,
        chatClientRequest = other.chatClientRequest.copy()
    )

    override var userText: Map<String, Any>.() -> String?
            by chatClientRequest::userText

    override var systemText: Map<String, Any>.() -> String?
            by chatClientRequest::systemText

    override fun user(userScope: PromptUserScope.() -> Unit) {
        DefaultPromptUserScope(this).apply(userScope)
    }

    override fun system(systemScope: PromptSystemScope.() -> Unit) {
        DefaultPromptSystemScope(this).apply(systemScope)
    }

    override fun enhancers(enhancersScope: EnhancersScope.() -> Unit) {
        DefaultEnhancersScope(this).apply(enhancersScope)
    }

    override fun functions(functionCallScope: FunctionCallScope.() -> Unit) {
        DefaultFunctionCallScope(this).apply(functionCallScope)
    }

}

class DefaultEnhancersScope(
    chatClientRequestScope: DefaultChatClientRequestScope
) : EnhancersScope {

    private val params: MutableMap<String, Any>
            by chatClientRequestScope.chatClientRequest::enhanceParams

    private val enhancers: MutableList<Enhancer>
            by chatClientRequestScope.chatClientRequest::enhancers

    override fun String.to(value: Any) {
        params[this] = value
    }

    override fun Enhancer.unaryPlus() {
        enhancers.add(this)
    }

    override fun List<Enhancer>.unaryPlus() {
        enhancers.addAll(this)
    }

}

class DefaultPromptUserScope(
    chatClientRequestScope: DefaultChatClientRequestScope
) : PromptUserScope {

    override var text: Map<String, Any>.() -> String?
            by chatClientRequestScope::userText

    private val params: MutableMap<String, Any>
            by chatClientRequestScope.chatClientRequest::userParams

    override fun String.to(value: Any) {
        params[this] = value
    }

}

class DefaultPromptSystemScope(
    chatClientRequestScope: DefaultChatClientRequestScope
) : PromptSystemScope {

    override var text: Map<String, Any>.() -> String?
            by chatClientRequestScope::systemText

    private val params: MutableMap<String, Any>
            by chatClientRequestScope.chatClientRequest::systemParams

    override fun String.to(value: Any) {
        params[this] = value
    }

}

class DefaultFunctionCallScope(
    chatClientRequestScope: DefaultChatClientRequestScope
) : FunctionCallScope {

    private val functionCalls: MutableList<FunctionCall>
            by chatClientRequestScope.chatClientRequest::functionCalls

    private val functionNames: MutableSet<String>
            by chatClientRequestScope.chatClientRequest::functionNames

    override fun FunctionCall.unaryPlus() {
        functionCalls.add(this)
    }

    override fun List<FunctionCall>.unaryPlus() {
        functionCalls.addAll(this)
    }

    override fun String.unaryPlus() {
        functionNames.add(this)
    }

    override fun KFunction<*>.unaryPlus() {
        functionNames.add(name)
    }

    override fun function(vararg functionNames: String) {
        this.functionNames.addAll(functionNames)
    }

}