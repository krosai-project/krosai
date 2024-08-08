package io.github.krosai.core.chat.model

import io.github.krosai.core.chat.prompt.ChatOptions
import io.github.krosai.core.chat.prompt.Prompt
import io.github.krosai.core.model.Model
import io.github.krosai.core.model.StreamingModel
import kotlinx.coroutines.flow.Flow

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