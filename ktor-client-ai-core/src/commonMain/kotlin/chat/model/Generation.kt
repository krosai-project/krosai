package io.kamo.ktor.client.ai.core.chat.model

import io.kamo.ktor.client.ai.core.chat.message.Message
import io.kamo.ktor.client.ai.core.model.ModelResult
import io.kamo.ktor.client.ai.core.model.ResultMetadata

data class Generation(
    private val text: String,
    private val properties: Map<String, Any> = emptyMap()
) : ModelResult<Message.Assistant> {


    override val output: Message.Assistant = Message.Assistant(text, properties)

    override val resultMetadata: ResultMetadata = ResultMetadata.NULL

}