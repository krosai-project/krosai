package io.github.krosai.openai.api.image

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OpenAiImageRequest(
    @SerialName("prompt") val prompt: String,
    @SerialName("model") val model: String,
    @SerialName("n") val n: Int? = null,
    @SerialName("quality") val quality: String? = null,
    @SerialName("response_format") val responseFormat: String? = null,
    @SerialName("size") val size: String? = null,
    @SerialName("style") val style: String? = null,
    @SerialName("user") val user: String? = null
) {
    constructor(prompt: String, model: String) : this(
        prompt, model, null, null, null, null, null, null
    )
}