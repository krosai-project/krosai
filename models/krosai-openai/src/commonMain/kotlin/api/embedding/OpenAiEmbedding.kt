package io.github.krosai.openai.api.embedding

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents an embedding vector returned by embedding endpoint.
 *
 * @param index The index of the embedding in the list of embeddings.
 * @param embedding The embedding vector, which is a list of floats. The length of
 * vector depends on the model.
 * @param object The object type, which is always 'embedding'.
 */
@Serializable
data class OpenAiEmbedding(
    @SerialName("index") val index: Int,
    @SerialName("embedding") val output: List<Double>,
    @SerialName("object") val obj: String = "embedding"
)