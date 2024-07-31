package io.kamo.ktor.client.ai.openai.api

import io.ktor.client.*
import io.ktor.client.plugins.sse.*
import io.ktor.client.request.*
import io.ktor.http.*

class OpenAiApi(
    baseUrl: String,
    private val apiKey: String,
    private val httpClient: HttpClient,
) {
    private val requestUrl = "$baseUrl/v1/chat/completions"

    suspend fun call(request: ChatCompletionRequest) = httpClient.post(block = createHttpRequest(request))

    suspend fun stream(request: ChatCompletionRequest) =
        httpClient.serverSentEventsSession(block = createHttpRequest(request))

    private fun createHttpRequest(request: ChatCompletionRequest): HttpRequestBuilder.() -> Unit = {
        url(requestUrl)
        contentType(ContentType.Application.Json)
        setBody(request)
        bearerAuth(apiKey)
    }

}