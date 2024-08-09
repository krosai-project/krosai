package io.github.krosai.core.factory

import io.github.krosai.core.chat.client.ChatClient
import io.github.krosai.core.chat.client.ChatClientRequestDefinition
import io.github.krosai.core.chat.client.DefaultChatClient
import io.github.krosai.core.chat.client.DefaultChatClientRequestScope
import io.github.krosai.core.chat.model.ChatModel
import io.github.krosai.core.embedding.model.EmbeddingModel
import io.github.krosai.core.image.ImageModel

/**
 * The interface for a factory that creates different models and a chat client.
 *
 *
 * @author KAMOsama
 */
interface ModelFactory {

    fun createChatModel(): ChatModel

    fun createEmbeddingModel(): EmbeddingModel = TODO()

    fun createImageModel(): ImageModel

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

