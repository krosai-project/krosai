package io.kamo.ktor.client.ai.openai.config

import io.kamo.ktor.client.ai.core.createAiPlugin
import io.kamo.ktor.client.ai.openai.model.OpenAiChatModel

data class OpenAiConfig(
    var baseUrl: String,
    var apiKey: String?,
    var chatOptions: OpenAiOptions,
    var embeddingOptions: OpenAiOptions
)

val OpenAi = createAiPlugin("OpenAi", ::OpenAiConfigBuilder) {
    val openAiConfig = this.config.build()
    chatModel = OpenAiChatModel(openAiConfig.chatOptions, client)

}


class OpenAiConfigBuilder {

    var baseUrl: String = "https://api.openai.com"

    var apiKey: String? = null

    val chatOptions: OpenAiOptionsBuilder = OpenAiOptionsBuilder()

    val embeddingOptions: OpenAiOptionsBuilder = OpenAiOptionsBuilder()

    fun chatOptions(build: OpenAiOptionsBuilder.() -> Unit) = chatOptions.apply(build)

    fun embeddingOptions(build: OpenAiOptionsBuilder.() -> Unit) = embeddingOptions.apply(build)

    fun build(): OpenAiConfig {

        return OpenAiConfig(
            baseUrl = baseUrl,
            apiKey = apiKey,
            chatOptions = chatOptions.build(baseUrl, apiKey),
            embeddingOptions = embeddingOptions.build(baseUrl, apiKey)
        )
    }
}

class OpenAiOptionsBuilder {
    var baseUrl: String? = null
    var apiKey: String? = null
    var model: String? = null
    fun build(
        defaultBaseUrl: String,
        defaultApiKey: String?,
    ): OpenAiOptions {
        return OpenAiOptions(
            baseUrl = baseUrl ?: defaultBaseUrl,
            apiKey = apiKey ?: defaultApiKey!!,
            model = model.orEmpty()
        )
    }
}

data class OpenAiOptions(
    val baseUrl: String,
    val apiKey: String,
    val model: String
)
