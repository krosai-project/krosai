package io.github.krosai.openai.factory

import io.github.krosai.core.chat.function.FunctionCall
import io.github.krosai.core.chat.model.ChatModel
import io.github.krosai.core.embedding.model.EmbeddingModel
import io.github.krosai.core.factory.ModelFactory
import io.github.krosai.core.factory.createModelFactory
import io.github.krosai.openai.api.OpenAiApi
import io.github.krosai.openai.model.OpenAiChatModel
import io.github.krosai.openai.model.OpenAiEmbeddingModel
import io.ktor.client.*

val OpenAI = createModelFactory(
    "OpenAI",
    ::OpenAIModelFactoryConfig
) { context ->
    return@createModelFactory OpenAIModelFactory(this, context::getFunctionCallsByName)
}

class OpenAIModelFactory(
    private val config: OpenAIModelFactoryConfig,
    private val getFunctionCall: (Set<String>) -> List<FunctionCall>
) : ModelFactory {

    private val client: HttpClient by lazy {
        HttpClient(block = config.clientBlock)
    }

    override fun createChatModel(): ChatModel {
        val chatOptions = config.chatOptions

        val api = OpenAiApi(
            config.baseUrl,
            config.apiKey ?: error("OpenAI API key is required"),
            client
        )

        return OpenAiChatModel(chatOptions, api, getFunctionCall)
    }

    override fun createEmbeddingModel(): EmbeddingModel {
        val embeddingOptions = config.embeddingOptions

        val api = OpenAiApi(
            config.baseUrl,
            config.apiKey ?: error("OpenAI API key is required"),
            client
        )

        return OpenAiEmbeddingModel(api, embeddingOptions)
    }
}

