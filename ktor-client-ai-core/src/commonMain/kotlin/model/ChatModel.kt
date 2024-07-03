package io.kamo.ktor.client.ai.core.model

import io.kamo.ktor.client.ai.core.Prompt
import kotlinx.coroutines.flow.Flow
import model.Model
import model.StreamingModel

interface ChatModel: Model<Prompt, String>, StreamingModel<String, String> {

    override suspend fun call(request: Prompt): String

    override fun stream(request: String): Flow<String>

}