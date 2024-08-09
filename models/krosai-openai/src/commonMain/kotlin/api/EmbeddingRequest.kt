package io.github.krosai.openai.api

import kotlinx.serialization.Serializable

/**
 * Creates an embedding vector representing the input text.
 *
 * @param input Input text to embed, encoded as a string or array of tokens. To embed
 * multiple inputs in a single request, pass an array of strings or array of token
 * arrays. The input must not exceed the max input tokens for the model (8192 tokens
 * for text-embedding-ada-002), cannot be an empty string, and any array must be 2048
 * dimensions or less.
 * @param model ID of the model to use.
 * @param encodingFormat The format to return the embeddings in. Can be either float
 * or base64.
 * @param dimensions The number of dimensions the resulting output embeddings should
 * have. Only supported in text-embedding-3 and later models.
 * @param user A unique identifier representing your end-user, which can help OpenAI
 * to monitor and detect abuse.
 */
@Serializable
data class EmbeddingRequest<T>(
    val input: T,
    val model: String = "text-embedding-ada-002",
    val encodingFormat: String = "float",
    val dimensions: Int? = null,
    val user: String? = null
)
