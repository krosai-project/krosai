package io.github.krosai.client.ai.core.factory

import io.github.krosai.client.ai.core.chat.client.ChatClient
import io.github.krosai.client.ai.core.chat.client.ChatClientRequestDefinition
import io.github.krosai.client.ai.core.chat.client.DefaultChatClient
import io.github.krosai.client.ai.core.chat.client.DefaultChatClientRequestScope
import io.github.krosai.client.ai.core.chat.model.ChatModel
import io.github.krosai.client.ai.core.embedding.model.EmbeddingModel


interface ModelFactory {

    fun createChatModel(): ChatModel

    fun createEmbeddingModel(): EmbeddingModel = TODO()

    fun createChatClient(scope: ChatClientRequestDefinition = null): ChatClient {
        val chatModel = createChatModel()
        val defaultRequest = DefaultChatClientRequestScope(chatModel, null).also { scope?.invoke(it) }
        return DefaultChatClient(chatModel, defaultRequest)
    }

}

interface ModelFactoryBuilder<Config, ModelFactory> {

    val id: String

    val config: Config

    fun install(factoryContext: ModelFactoryContext)

}

fun <Config : Any, M : ModelFactory> createModelFactory(
    id: String,
    createConfiguration: () -> Config,
    builder: Config.(context: ModelFactoryContext) -> M
): ModelFactoryBuilder<Config, M> {
    return object : ModelFactoryBuilder<Config, M> {

        override val id: String = id

        override val config: Config = createConfiguration()

        override fun install(factoryContext: ModelFactoryContext) =
            builder.invoke(config, factoryContext).let { factoryContext.register(this, it) }

    }
}

