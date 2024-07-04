package io.kamo.ktor.client.ai.openai.model

import io.kamo.ktor.client.ai.core.Prompt
import io.kamo.ktor.client.ai.core.model.ChatModel
import io.kamo.ktor.client.ai.openai.api.ChatCompletion
import io.kamo.ktor.client.ai.openai.api.ChatCompletionRequest
import io.kamo.ktor.client.ai.openai.config.OpenAiOptions
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.flow.Flow

class OpenAiChatModel(
    private val options: OpenAiOptions,
    private val httpClient: HttpClient
) : ChatModel {

    override suspend fun call(request: Prompt): String {
        return kotlin.run {
            httpClient.post {
                url("${options.baseUrl}/v1/chat/completions")
                contentType(ContentType.Application.Json)
                setBody(ChatCompletionRequest.build(options, request, false))
                bearerAuth(options.apiKey)
            }.body<ChatCompletion>().toString()
//                .bodyAsText()
        }
    }

    override fun stream(request: String): Flow<String> {
        TODO("Not yet implemented")
    }

}

//{
//  "id": "chatcmpl-9hBQDNOWPnolxTAHvScpoE4BltBR8",
//  "object": "chat.completion",
//  "created": 1720079037,
//  "model": "gpt-4o-2024-05-13",
//  "choices": [
//    {
//      "index": 0,
//      "message": {
//        "role": "assistant",
//        "content": "你好！有什么我可以帮你的吗？"
//      },
//      "logprobs": null,
//      "finish_reason": "stop"
//    }
//  ],
//  "usage": {
//    "prompt_tokens": 8,
//    "completion_tokens": 9,
//    "total_tokens": 17
//  },
//  "system_fingerprint": "fp_d576307f90"
//}