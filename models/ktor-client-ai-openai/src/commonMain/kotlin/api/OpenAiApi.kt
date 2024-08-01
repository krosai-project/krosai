package io.kamo.ktor.client.ai.openai.api

import io.kamo.ktor.client.ai.core.util.DefaultJsonConverter
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.sse.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.flow.*

/**
 * Whether to stream back partial progress.
 * If set, tokens will be sent as data-only server-sent events as they become available,
 * with the stream terminated by a data: [[DONE]] message.
 */
private val SSE_PREDICATE: (String) -> Boolean = { it.isNotEmpty() && it != "[DONE]" }

class OpenAiApi(
    baseUrl: String,
    private val apiKey: String,
    private val httpClient: HttpClient,
) {
    private val requestUrl = "$baseUrl/v1/chat/completions"

    suspend fun call(request: ChatCompletionRequest) =
        httpClient.post(block = createHttpRequest(request))
            .body<ChatCompletion>()

    suspend fun stream(request: ChatCompletionRequest) =
        httpClient.serverSentEventsSession(block = createHttpRequest(request))
            .incoming
            .mapNotNull { it.data }
            .filter(SSE_PREDICATE)
            .map { DefaultJsonConverter.decodeFromString<ChatCompletionChunk>(it) }

    private fun createHttpRequest(request: ChatCompletionRequest): HttpRequestBuilder.() -> Unit = {
        url(requestUrl)
        contentType(ContentType.Application.Json)
        setBody(request)
        bearerAuth(apiKey)
    }

}