package org.krosai.core.chat.metadata

import org.krosai.core.model.ResultMetadata

/**
 * Represents the metadata associated with the chat generation result.
 *
 * @param T The type of the content filter metadata.
 */
interface ChatGenerationMetadata<T> : ResultMetadata {

    /**
     * Represents the metadata associated with the content filter for chat generation.
     *
     * @property contentFilterMetadata The metadata associated with the content filter.
     * @property finishReason The reason for completing the generation.
     * @param T The type of the content filter metadata.
     */
    val contentFilterMetadata: T?

    /**
     * Represents the reason for completing the generation of chat.
     *
     * @property finishReason The reason for completing the generation.
     */
    val finishReason: String?

    companion object {
        fun <T> from(finishReason: String?, contentFilterMetadata: T?): ChatGenerationMetadata<T> {
            return object : ChatGenerationMetadata<T> {
                override val contentFilterMetadata: T? = contentFilterMetadata
                override val finishReason: String? = finishReason
            }
        }

    }

    data object Null : ChatGenerationMetadata<Nothing> {
        override val contentFilterMetadata: Nothing? = null
        override val finishReason: String? = null
    }

}