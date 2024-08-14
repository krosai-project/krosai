package org.krosai.core.chat.model

import org.krosai.core.chat.message.Message
import org.krosai.core.chat.metadata.ChatGenerationMetadata
import org.krosai.core.model.ModelResult

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