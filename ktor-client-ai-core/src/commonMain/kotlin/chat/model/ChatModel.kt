package io.kamo.ktor.client.ai.core.chat.model

import io.kamo.ktor.client.ai.core.chat.prompt.ChatOptions
import io.kamo.ktor.client.ai.core.chat.prompt.Prompt
import io.kamo.ktor.client.ai.core.model.Model
import io.kamo.ktor.client.ai.core.model.StreamingModel
import kotlinx.coroutines.flow.Flow


interface ChatModel: Model<Prompt, ChatResponse>, StreamingModel<Prompt, ChatResponse> {

    val defaultChatOptions: ChatOptions

    override suspend fun call(request: Prompt): ChatResponse

    override suspend fun stream(request: Prompt): Flow<ChatResponse>

}