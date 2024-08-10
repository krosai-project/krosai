package io.github.krosai.openai.api.embedding

import io.github.krosai.openai.api.OpenAiUsage
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * List of multiple embedding responses.
 *
 * @param <T> Type of the entities in the data list.
 * @param object Must have value "list".
 * @param data List of entities.
 * @param model ID of the model to use.
 * @param usage Usage statistics for the completion request.
 */
@Serializable
data class EmbeddingList<T>(
    @SerialName("object") val obj: String,
    @SerialName("data") val data: List<T>,
    @SerialName("model") val model: String,
    @SerialName("usage") val usage: OpenAiUsage
)