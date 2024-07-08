package io.kamo.ktor.client.ai.core.model

import io.kamo.ktor.client.ai.core.Prompt
import kotlinx.coroutines.flow.Flow
import model.Model
import model.StreamingModel

interface ChatModel: Model<Prompt, ChatResponse>, StreamingModel<Prompt, ChatResponse> {

    override suspend fun call(request: Prompt): ChatResponse

    override suspend fun stream(request: Prompt): Flow<ChatResponse>

}