package org.krosai.core.chat.model

import org.krosai.core.chat.metadata.ChatResponseMetadata
import org.krosai.core.model.ModelResponse

/**
 * Represents a response from a chat model.
 *
 * @property results The list of generated outputs by the AI model.
 * @property responseMetadata The response metadata associated with the AI model's response.
 * @property result The result of the AI model.
 * @property content The content of the result output.
 *
 * @author KAMOsama
 */
data class ChatResponse(
    override val results: List<Generation>,
    override var responseMetadata: ChatResponseMetadata = ChatResponseMetadata()
) : ModelResponse<Generation> {

    override val result: Generation
        get() = results.first()

    val content = result.output.content

}
