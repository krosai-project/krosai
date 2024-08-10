package io.github.krosai.openai.api

import io.github.krosai.core.util.DefaultJsonConverter
import io.github.krosai.openai.api.image.OpenAiImageRequest
import io.github.krosai.openai.api.image.OpenAiImageResponse
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.sse.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.flow.*



class OpenAiApi(
    @PublishedApi
    internal val baseUrl: String,
    @PublishedApi
    internal val apiKey: String,
    @PublishedApi
    internal val httpClient: HttpClient,
) {
    companion object {

        /**
         * Whether to stream back partial progress.
         * If set, tokens will be sent as data-only server-sent events as they become available,
         * with the stream terminated by a data: [[DONE]] message.
         */
        private val SSE_PREDICATE: (String) -> Boolean = { it.isNotEmpty() && it != "[DONE]" }

        @PublishedApi
        internal const val CHAT_PATH = "chat/completions"

        @PublishedApi
        internal const val IMAGE_PATH = "images/generations"

        @PublishedApi
        internal const val EMBEDDING_PATH = "embeddings"

    }

    suspend fun call(request: ChatCompletionRequest): ChatCompletion =
        httpClient.post(block = createHttpRequest(CHAT_PATH, request))
            .body()

    suspend fun stream(request: ChatCompletionRequest): Flow<ChatCompletionChunk> =
        httpClient.serverSentEventsSession(block = createHttpRequest(CHAT_PATH, request))
            .incoming
            .mapNotNull { it.data }
            .takeWhile { SSE_PREDICATE(it) }
            .filter(SSE_PREDICATE)
            .map { DefaultJsonConverter.decodeFromString<ChatCompletionChunk>(it) }
            .mergeChunks()

    suspend inline fun <reified T> embeddings(request: EmbeddingRequest<T>): EmbeddingList<Embedding> {
        return httpClient.post(block = createHttpRequest(EMBEDDING_PATH, request))
            .body()
    }


    suspend fun createImage(request: OpenAiImageRequest): OpenAiImageResponse =
        httpClient.post(block = createHttpRequest(IMAGE_PATH, request))
            .body()

    @PublishedApi
    internal inline fun <reified T> createHttpRequest(requestPath: String, request: T): HttpRequestBuilder.() -> Unit =
        {
        method = HttpMethod.Post
        url("$baseUrl$requestPath")
        contentType(ContentType.Application.Json)
        setBody(request)
        bearerAuth(apiKey)
    }

}

/**
 * Merges consecutive chunks of ChatCompletionChunk into a single chunk.
 *
 * @return A Flow of ChatCompletionChunk where consecutive chunks are merged into one.
 */
suspend fun Flow<ChatCompletionChunk>.mergeChunks(): Flow<ChatCompletionChunk> {
    return flow {
        var prev: ChatCompletionChunk? = null
        collect {

            val merged = prev?.merge(it) ?: it

            if (!isToolFunctionCall(it) || isToolFunctionCallFinish(it)) {
                emit(merged)
                prev = null
            } else {
                prev = merged
            }

        }
    }
}

fun ChatCompletionChunk.merge(currentChunk: ChatCompletionChunk): ChatCompletionChunk {
    return copy(
        id = this.id,
        systemFingerprint = currentChunk.systemFingerprint ?: this.systemFingerprint.orEmpty(),
        choices = this.choices.merge(currentChunk.choices)
    )
}

fun List<ChatCompletionChunk.ChunkChoice>.merge(currentChoice: List<ChatCompletionChunk.ChunkChoice>): List<ChatCompletionChunk.ChunkChoice> {
    val previous = this.first()
    val current = currentChoice.first()
    return listOf(
        ChatCompletionChunk.ChunkChoice(
            finishReason = current.finishReason ?: previous.finishReason,
            index = current.index,
            delta = previous.delta.merge(current.delta),
            logprobs = current.logprobs ?: previous.logprobs
        )
    )
}

fun ChatCompletionMessage.merge(currentMessage: ChatCompletionMessage): ChatCompletionMessage {
    val lastPrevToolCall = this.toolCalls?.lastOrNull()
    val toolCalls = mutableListOf<ChatCompletionMessage.ToolCall>()

    toolCalls.addAll(
        this.toolCalls?.dropLast(1).orEmpty()
    )

    if (currentMessage.toolCalls?.isNotEmpty() == true) {
        val currentToolCall = currentMessage.toolCalls.first()
        if (currentToolCall.id != null) {
            lastPrevToolCall?.also(toolCalls::add)
            toolCalls.add(currentToolCall)
        } else {
            toolCalls.add(lastPrevToolCall?.merge(currentToolCall) ?: currentToolCall)
        }
    } else {
        lastPrevToolCall?.also(toolCalls::add)
    }
    return copy(
        content = currentMessage.content ?: this.content.orEmpty(),
        role = currentMessage.role,
        name = currentMessage.name ?: this.name.orEmpty(),
        toolCallId = currentMessage.toolCallId ?: this.toolCallId.orEmpty(),
        toolCalls = toolCalls
    )
}

fun ChatCompletionMessage.ToolCall.merge(currentToolCall: ChatCompletionMessage.ToolCall): ChatCompletionMessage.ToolCall {
    return copy(
        id = currentToolCall.id ?: this.id.orEmpty(),
        type = currentToolCall.type,
        function = this.function.merge(currentToolCall.function),
    )
}

fun ChatCompletionMessage.ChatCompletionFunction.merge(currentFunction: ChatCompletionMessage.ChatCompletionFunction?): ChatCompletionMessage.ChatCompletionFunction {
    return copy(
        name = currentFunction?.name ?: this.name.orEmpty(),
        arguments = this.arguments + currentFunction?.arguments.orEmpty(),
    )
}

fun isToolFunctionCall(chunk: ChatCompletionChunk): Boolean {
    return chunk.choices.isNotEmpty() && chunk.choices.first().delta.toolCalls?.isNotEmpty() ?: false
}

fun isToolFunctionCallFinish(chunk: ChatCompletionChunk): Boolean {
    return chunk.choices.first().finishReason == ChatCompletionFinishReason.TOOL_CALLS
}