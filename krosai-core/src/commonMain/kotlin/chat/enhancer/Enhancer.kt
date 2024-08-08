package io.github.krosai.core.chat.enhancer

import io.github.krosai.core.chat.client.ChatClientRequest
import io.github.krosai.core.chat.model.ChatResponse
import kotlinx.coroutines.flow.Flow

/**
 * The Enhancer interface defines the operations related to enhancing the chat request and response.
 *
 * @author KAMOsama
 */
interface Enhancer {

    /**
     * Enhance the request.
     * @param request The request to enhance.
     * @return The enhanced request.
     */
    fun enhanceRequest(request: ChatClientRequest): ChatClientRequest

    /**
     * Enhance the response.
     * @param response The response to enhance.
     * @param enhancerParams  taken from the enhancerParams of the request parameter.
     * @return The enhanced response.
     */
    fun enhanceResponse(response: ChatResponse, enhancerParams: Map<String, Any>): ChatResponse

    /**
     * Enhance the response flow.
     * @param responseFlow The response flow to enhance.
     * @param enhancerParams  taken from the enhancerParams of the request parameter.
     * @return The enhanced response flow.
     */
    fun enhanceResponse(responseFlow: Flow<ChatResponse>, enhancerParams: Map<String, Any>): Flow<ChatResponse>

}

/**
 * Extension function for enhancing a list of enhancers.
 */
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