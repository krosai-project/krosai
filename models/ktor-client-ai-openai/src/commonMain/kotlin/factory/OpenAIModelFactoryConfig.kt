package io.kamo.ktor.client.ai.openai.factory

import io.kamo.ktor.client.ai.core.chat.function.FunctionCall
import io.kamo.ktor.client.ai.core.util.DefaultJsonConverter
import io.kamo.ktor.client.ai.openai.config.OpenAiOptionsBuilder
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

    val functionCalls = mutableListOf<FunctionCall>()

    var baseUrl: String = "https://api.openai.com"

    var model = "gpt-3.5-turbo"

    var apiKey: String? = null

    private val chatOptions: OpenAiOptionsBuilder = OpenAiOptionsBuilder()

    fun chatOptions(build: OpenAiOptionsBuilder.() -> Unit) = chatOptions.apply(build)

}