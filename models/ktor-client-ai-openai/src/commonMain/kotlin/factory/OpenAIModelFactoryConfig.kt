package io.kamo.ktor.client.ai.openai.factory

import io.kamo.ktor.client.ai.core.chat.function.FunctionCall
import io.kamo.ktor.client.ai.openai.config.OpenAiOptionsBuilder
import io.ktor.client.*

class OpenAIModelFactoryConfig {

    var client: HttpClient? = null

    val functionCalls = mutableListOf<FunctionCall>()

    var baseUrl: String = "https://api.openai.com"

    var model = "gpt-3.5-turbo"

    var apiKey: String? = null

    private val chatOptions: OpenAiOptionsBuilder = OpenAiOptionsBuilder()

    fun chatOptions(build: OpenAiOptionsBuilder.() -> Unit) = chatOptions.apply(build)

}