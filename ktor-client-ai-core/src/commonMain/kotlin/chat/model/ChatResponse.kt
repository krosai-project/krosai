package io.kamo.ktor.client.ai.core.chat.model

import io.kamo.ktor.client.ai.core.chat.message.Message
import io.kamo.ktor.client.ai.core.model.ModelResponse
import io.kamo.ktor.client.ai.core.model.ModelResult
import io.kamo.ktor.client.ai.core.model.ResponseMetadata
import io.kamo.ktor.client.ai.core.model.ResultMetadata

data class ChatResponse(
    override val results: List<Generation>,
    override var responseMetadata: ResponseMetadata = ResponseMetadata.NULL
) : ModelResponse<Generation> {

    override val result: Generation
        get() = results.first()
}

data class Generation(
    private val text: String,
    private val properties: Map<String, Any> = emptyMap()
) : ModelResult<Message.Assistant> {


    override val output: Message.Assistant = Message.Assistant(text, properties)

    override val resultMetadata: ResultMetadata = ResultMetadata.NULL

}