@file:Suppress("unused")

package org.krosai.core.embedding.model

import org.krosai.core.model.Model

/**
 * Represents an embedding model that can calculate embeddings for given texts.
 */
interface EmbeddingModel: Model<EmbeddingRequest, EmbeddingResponse> {

    // TODO: Support for media types

    /**
     * Calculates the embedding for the given texts.
     *
     * @param texts the texts to calculate the embedding for
     * @return the list of embeddings for each text as a list of lists of double values
     */
    suspend fun embed(vararg texts: String): List<List<Double>> =
        call(EmbeddingRequest(texts.toList(), DefaultEmbeddingOptions()))
            .results
            .map { it.output }
            .toList()


    /**
     * Calculates the embedding for the given text.
     *
     * @param text the text to calculate the embedding for
     * @return the embedding for the text as a list of double values
     */
    suspend fun embed(text: String): List<Double> = this.embed(*arrayOf(text)).first()

    /**
     * Returns the dimension of the embedding produced by the AI model.
     *
     * @return the dimension of the embedding
     */
    suspend fun dimension(): Int = embed("Test String").size

}