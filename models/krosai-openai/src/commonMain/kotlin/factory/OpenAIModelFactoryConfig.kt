package io.github.krosai.openai.factory

import io.github.krosai.core.util.DefaultJsonConverter
import io.github.krosai.openai.options.OpenAiChatOptions
import io.github.krosai.openai.options.OpenAiEmbeddingOptions
import io.github.krosai.openai.options.OpenAiImageOptions
import io.ktor.client.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.sse.*
import io.ktor.serialization.kotlinx.json.*

class OpenAIModelFactoryConfig {

    var clientBlock: HttpClientConfig<*>.() -> Unit = {
        install(ContentNegotiation) {
            json(DefaultJsonConverter)
        }
        install(SSE)
    }


    var baseUrl: String = "https://api.openai.com"

    var apiKey: String? = null

    internal val chatOptions: OpenAiChatOptions = OpenAiChatOptions()

    internal val embeddingOptions: OpenAiEmbeddingOptions = OpenAiEmbeddingOptions()

    internal val imageOptions: OpenAiImageOptions = OpenAiImageOptions()

    fun chatOptions(build: OpenAiChatOptions.() -> Unit) = chatOptions.apply(build)

    fun embeddingOptions(build: OpenAiEmbeddingOptions.() -> Unit) = embeddingOptions.apply(build)

    fun imageOptions(build: OpenAiImageOptions.() -> Unit) = imageOptions.apply(build)

}