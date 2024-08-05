package io.kamo.ktor.client.ai.core.chat.client

import io.kamo.ktor.client.ai.core.chat.model.ChatResponse
import kotlinx.coroutines.flow.Flow

interface Enhancer {

    fun enhanceRequest(request: ChatClientRequest): ChatClientRequest

    fun enhanceResponse(response: ChatResponse): ChatResponse

    fun enhanceResponse(responseFlow: Flow<ChatResponse>): Flow<ChatResponse>

}

internal fun <T> enhanceProcessor(
    enhancers: List<Enhancer>,
    default: T,
    call: Enhancer.(T) -> T
): T {
    var enhanced = default
    enhancers.toList().forEach {
        enhanced = it.call(enhanced)
    }
    return enhanced
}