package org.krosai.core.chat.model

import kotlinx.coroutines.flow.Flow
import org.krosai.core.chat.prompt.ChatOptions
import org.krosai.core.chat.prompt.Prompt
import org.krosai.core.model.Model
import org.krosai.core.model.StreamingModel

/**
 * The interface for a chat model.
 *
 * @author KAMOsama
 */
interface ChatModel: Model<Prompt, ChatResponse>, StreamingModel<Prompt, ChatResponse> {

    /**
     * The default chat options.
     */
    val defaultChatOptions: ChatOptions

    /**
     * Call the model with the given request.
     * @return The chat response.
     */
    override suspend fun call(request: Prompt): ChatResponse

    /**
     * Call the model with the given request.
     * @return A flow of chat responses.
     */
    override suspend fun stream(request: Prompt): Flow<ChatResponse>

}