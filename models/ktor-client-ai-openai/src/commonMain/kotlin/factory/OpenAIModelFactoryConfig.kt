package io.kamo.ktor.client.ai.openai.factory

import io.kamo.ktor.client.ai.core.chat.function.FunctionCall
import io.kamo.ktor.client.ai.core.util.DefaultJsonConverter
import io.kamo.ktor.client.ai.openai.options.OpenAiChatOptions
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

    fun chatOptions(build: OpenAiChatOptions.() -> Unit) = chatOptions.apply(build)

}