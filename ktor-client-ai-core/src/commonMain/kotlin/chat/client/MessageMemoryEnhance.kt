package io.kamo.ktor.client.ai.core.chat.client

import io.kamo.ktor.client.ai.core.chat.message.Message
import io.kamo.ktor.client.ai.core.chat.model.ChatResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach

class MessageMemoryEnhance : Enhancer {

    private val memory: MutableList<Message> = mutableListOf()

    override fun enhanceRequest(request: ChatClientRequest): ChatClientRequest {
        val clientRequest = request.copy(
            messages = memory.toMutableList()
        )
        request.userText(request.userParams)
            ?.let { memory.add(Message.User(it)) }
        return clientRequest
    }

    override fun enhanceResponse(response: ChatResponse): ChatResponse =
        response.also {
            memory += it.result.output
        }

    override fun enhanceResponse(responseFlow: Flow<ChatResponse>): Flow<ChatResponse> {
        val stringBuilder = StringBuilder()
        return responseFlow
            .onEach { chatResponse ->
                stringBuilder.append(chatResponse.result.output.content)
            }.onCompletion { chatResponse ->
                if (chatResponse == null) {
                    val assistant = Message.Assistant(stringBuilder.toString())
                    memory += assistant
                }
            }
    }

}