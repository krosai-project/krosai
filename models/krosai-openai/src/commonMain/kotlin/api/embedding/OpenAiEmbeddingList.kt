package org.krosai.openai.api.embedding

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.krosai.openai.api.Usage

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
data class OpenAiEmbeddingList<T>(
    @SerialName("object") val obj: String,
    @SerialName("data") val data: List<T>,
    @SerialName("model") val model: String,
    @SerialName("usage") val usage: Usage
)