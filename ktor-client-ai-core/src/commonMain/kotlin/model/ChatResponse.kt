package io.kamo.ktor.client.ai.core.model

import io.kamo.ktor.client.ai.core.message.Message
import model.ModelResponse
import model.ModelResult
import model.ResponseMetadata
import model.ResultMetadata

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