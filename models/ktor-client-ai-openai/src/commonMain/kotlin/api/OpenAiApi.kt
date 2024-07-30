package io.kamo.ktor.client.ai.openai.api

import io.kamo.ktor.client.ai.core.chat.function.FunctionCall
import io.kamo.ktor.client.ai.core.chat.prompt.Prompt
import io.ktor.client.*
import io.ktor.client.plugins.sse.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*

class OpenAiApi(
    baseUrl: String,
    private val apiKey: String,
    private val httpClient: HttpClient,
    private val getFunctionCallNames: (Set<String>) -> List<FunctionCall>
) {
    private val requestUrl = "$baseUrl/v1/chat/completions"

    suspend fun call(request: Prompt) = httpClient.post {
        url(requestUrl)
        contentType(ContentType.Application.Json)
        setBody(ChatCompletionRequest.build(request, false, getFunctionCallNames))
        bearerAuth(apiKey)
    }.apply {
        println(this.bodyAsText())
    }

    suspend fun stream(request: Prompt) = httpClient.serverSentEventsSession {
        url(requestUrl)
        method = HttpMethod.Post
        contentType(ContentType.Application.Json)
        setBody(ChatCompletionRequest.build(request, true, getFunctionCallNames))
        bearerAuth(apiKey)
    }
}