package io.github.krosai.openai.factory

import io.github.krosai.core.chat.function.FunctionCall
import io.github.krosai.core.chat.model.ChatModel
import io.github.krosai.core.embedding.model.EmbeddingModel
import io.github.krosai.core.factory.ModelFactory
import io.github.krosai.core.factory.createModelFactory
import io.github.krosai.core.image.ImageModel
import io.github.krosai.openai.api.OpenAiApi
import io.github.krosai.openai.model.OpenAiChatModel
import io.github.krosai.openai.model.OpenAiEmbeddingModel
import io.github.krosai.openai.model.OpenAiImageModel
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

    private val api by lazy {
        val apiKey = checkNotNull(config.apiKey) {
            "OpenAI API key is required"
        }
        // support custom base url
        val baseUrl = buildString {
            append(config.baseUrl)
            if (last() != '/') append('/')
            if (contains(Regex("/v[0-9]+/")).not()) append("v1/")
            if (last() != '/') append('/')
        }
        OpenAiApi(
            baseUrl,
            apiKey,
            client
        )
    }

    override fun createImageModel(): ImageModel =
        OpenAiImageModel(api, config.imageOptions)


    override fun createChatModel(): ChatModel =
        OpenAiChatModel(api, config.chatOptions, getFunctionCall)

    override fun createEmbeddingModel(): EmbeddingModel =
        OpenAiEmbeddingModel(api, config.embeddingOptions)

}

