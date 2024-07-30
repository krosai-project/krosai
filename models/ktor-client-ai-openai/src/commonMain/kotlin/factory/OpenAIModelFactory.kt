package io.kamo.ktor.client.ai.openai.factory

import io.kamo.ktor.client.ai.core.chat.model.ChatModel
import io.kamo.ktor.client.ai.core.factory.ModelFactory
import io.kamo.ktor.client.ai.core.factory.createModelFactory
import io.kamo.ktor.client.ai.openai.config.OpenAiOptions
import io.kamo.ktor.client.ai.openai.model.OpenAiChatModel
import io.ktor.client.*

val OpenAI = createModelFactory(
    "OpenAI",
    ::OpenAIModelFactoryConfig
) {
    return@createModelFactory OpenAIModelFactory(this)
}

class OpenAIModelFactory(
    private val config: OpenAIModelFactoryConfig
) : ModelFactory {

    val client: HttpClient by lazy {
        HttpClient(block = config.clientBlock)
    }

    override fun createChatModel(): ChatModel {
        val openAiOptions = OpenAiOptions(
            baseUrl = config.baseUrl,
            apiKey = config.apiKey!!,
            model = config.model,
        )
        return OpenAiChatModel(openAiOptions, client)
    }

}
