package io.github.krosai.core.chat.model

import io.github.krosai.core.chat.message.Message
import io.github.krosai.core.model.ModelResult
import io.github.krosai.core.model.ResultMetadata

/**
 * A model result for a [Generation].
 * @param output The generated message.
 * @param resultMetadata The metadata of the result.
 * @see ModelResult
 *
 * @author KAMOsama
 */
data class Generation(
    override val output: Message.Assistant,
    override val resultMetadata: ResultMetadata = ResultMetadata.NULL
) : ModelResult<Message.Assistant>