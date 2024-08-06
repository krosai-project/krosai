package io.github.krosai.core.chat.model

import io.github.krosai.core.chat.message.Message
import io.github.krosai.core.model.ModelResult
import io.github.krosai.core.model.ResultMetadata

data class Generation(
    override val output: Message.Assistant,
    override val resultMetadata: ResultMetadata = ResultMetadata.NULL
) : ModelResult<Message.Assistant>