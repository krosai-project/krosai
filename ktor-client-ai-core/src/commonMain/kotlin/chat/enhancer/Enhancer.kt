package io.kamo.ktor.client.ai.core.chat.enhancer

import io.kamo.ktor.client.ai.core.chat.client.ChatClientRequest
import io.kamo.ktor.client.ai.core.chat.model.ChatResponse
import kotlinx.coroutines.flow.Flow

interface Enhancer {

    fun enhanceRequest(request: ChatClientRequest): ChatClientRequest

    fun enhanceResponse(response: ChatResponse, enhancerParams: Map<String, Any>): ChatResponse

    fun enhanceResponse(responseFlow: Flow<ChatResponse>, enhancerParams: Map<String, Any>): Flow<ChatResponse>

}

internal fun <T> List<Enhancer>.enhancing(
    default: T,
    enhance: Enhancer.(T) -> T
): T {
    var enhanced = default
    this.toList().forEach {
        enhanced = it.enhance(enhanced)
    }
    return enhanced
}