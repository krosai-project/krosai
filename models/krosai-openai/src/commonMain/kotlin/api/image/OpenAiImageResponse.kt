package org.krosai.openai.api.image

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OpenAiImageResponse(
    @SerialName("created") val created: Long,
    @SerialName("data") val data: List<Data>
) {

    @Serializable
    data class Data(
        @SerialName("url") val url: String,
        @SerialName("b64_json") val b64Json: String? = null,
        @SerialName("revised_prompt") val revisedPrompt: String? = null
    )

}