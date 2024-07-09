package io.kamo.ktor.client.ai.core

import io.kamo.ktor.client.ai.core.message.Message
import io.kamo.ktor.client.ai.core.model.ChatModel
import io.kamo.ktor.client.ai.core.model.ChatResponse
import kotlinx.coroutines.flow.Flow


class DefaultChatClient(private val chatModel: ChatModel): ChatClient{


    override fun request(request: ChatClientRequestScope.() -> Unit): ChatClientRequestScope {
        return DefaultChatClientRequestScope(chatModel).apply(request)
    }

}

class DefaultChatClientRequestScope(
    private val chatModel: ChatModel
): ChatClientRequestScope {

    override var model: String = ""

    override var userText: String  = ""

    private val userParams: MutableMap<String, String> = mutableMapOf()

    override var systemText: String  = ""

    private val systemParams: MutableMap<String, String> = mutableMapOf()

    override fun user(userScope: PromptUserScope.() -> Unit) {
        val promptUser = DefaultPromptUserScope().apply(userScope)
        promptUser.text?.let { userText = it }
        promptUser.params.let { userParams.putAll(it) }
    }

    override fun system(systemScope: PromptSystemScope.() -> Unit) {
        val promptSystem = DefaultPromptSystemScope().apply(systemScope)
        promptSystem.text?.let { systemText = it }
        promptSystem.params.let { systemParams.putAll(it) }
    }

    override suspend fun call(): ChatResponse {

        systemParams.forEach { (k, v) ->
            systemText = systemText.replace("{$k}", v)
        }
        userParams.forEach { (k, v) ->
            userText = userText.replace("{$k}", v)
        }
        val prompt = Prompt(
            Message.User(userText),
            Message.System(systemText),
        )
        return chatModel.call(prompt)
    }

    override suspend fun stream(): Flow<ChatResponse> {
        systemParams.forEach { (k, v) ->
            systemText = systemText.replace("{$k}", v)
        }
        userParams.forEach { (k, v) ->
            userText = userText.replace("{$k}", v)
        }
        val prompt = Prompt(
            Message.User(userText),
            Message.System(systemText),
        )
        return chatModel.stream(prompt)
    }


}

class DefaultPromptUserScope: PromptUserScope {
    override var text: String?  = null
    override val params: MutableMap<String, String> = mutableMapOf()
    override fun params(vararg params: Pair<String, String>) {
        this.params.putAll(params)
    }
}

class DefaultPromptSystemScope: PromptSystemScope {
    override var text: String?  = null
    override val params: MutableMap<String, String> = mutableMapOf()
    override fun params(vararg params: Pair<String, String>) {
        this.params.putAll(params)
    }
}