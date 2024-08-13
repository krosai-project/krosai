package io.github.krosai.core.chat.model

import io.github.krosai.core.chat.message.Message
import io.github.krosai.core.chat.metadata.ChatGenerationMetadata
import io.github.krosai.core.model.ModelResult

/**
 * A model result for a [Generation].
 * @param output The generated message.
 * @param metadata The metadata of the result.
 * @see ModelResult
 *
 * @author KAMOsama
 */
data class Generation(
    override val output: Message.Assistant,
    override val metadata: ChatGenerationMetadata<*> = ChatGenerationMetadata.Null
) : ModelResult<Message.Assistant>