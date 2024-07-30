package io.kamo.ktor.client.ai.openai.config



data class OpenAiConfig(
    var baseUrl: String,
    var apiKey: String?,
    var chatOptions: OpenAiOptions,
    var embeddingOptions: OpenAiOptions
)



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
