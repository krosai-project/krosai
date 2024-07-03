package io.kamo.ktor.client.ai.openai.model

import io.kamo.ktor.client.ai.core.Prompt
import io.kamo.ktor.client.ai.core.model.ChatModel
import io.kamo.ktor.client.ai.openai.api.ChatCompletionRequest
import io.kamo.ktor.client.ai.openai.config.OpenAiOptions
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.flow.Flow

class OpenAiChatModel(
    private val options: OpenAiOptions,
    private val httpClient: HttpClient
) : ChatModel {

    override suspend fun call(request: Prompt): String {
        return kotlin.runCatching {
            httpClient.post {
                url("${options.baseUrl}/v1/chat/completions")
                contentType(ContentType.Application.Json)
                setBody(ChatCompletionRequest.build(options, request, false))
                bearerAuth(options.apiKey)
            }.bodyAsText()
        }.recover {
            it.cause?.printStackTrace()
            ""
        }.getOrThrow()
    }

    override fun stream(request: String): Flow<String> {
        TODO("Not yet implemented")
    }

}

