@file:Suppress("unused")

package io.github.krosai.core.embedding.model

import io.github.krosai.core.model.Model

interface EmbeddingModel: Model<EmbeddingRequest, EmbeddingResponse> {

    // TODO: Support for media types

    suspend fun embed(vararg texts: String): List<List<Double>> =
        call(EmbeddingRequest(texts.toList(), DefaultEmbeddingOptions()))
            .results
            .map { it.output }
            .toList()

    suspend fun embed(text: String): List<Double> = this.embed(*arrayOf(text)).first()

    suspend fun dimension(): Int = embed("Test String").size
}