package io.kamo.ktor.client.ai.core.chat.model

import io.kamo.ktor.client.ai.core.model.ModelResponse
import io.kamo.ktor.client.ai.core.model.ResponseMetadata

data class ChatResponse(
    override val results: List<Generation>,
    override var responseMetadata: ResponseMetadata = ResponseMetadata.NULL
) : ModelResponse<Generation> {

    override val result: Generation
        get() = results.first()
}