package io.github.krosai.core.chat.model

import io.github.krosai.core.model.ModelResponse
import io.github.krosai.core.model.ResponseMetadata

data class ChatResponse(
    override val results: List<Generation>,
    override var responseMetadata: ResponseMetadata = ResponseMetadata.NULL
) : ModelResponse<Generation> {

    override val result: Generation
        get() = results.first()

    val content = result.output.content

}
