package io.kamo.ktor.client.ai.core.chat.model

import io.kamo.ktor.client.ai.core.chat.message.Message
import io.kamo.ktor.client.ai.core.model.ModelResult
import io.kamo.ktor.client.ai.core.model.ResultMetadata

data class Generation(
    override val output: Message.Assistant,
    override val resultMetadata: ResultMetadata = ResultMetadata.NULL
) : ModelResult<Message.Assistant>