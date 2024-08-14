package org.krosai.openai.model

import org.krosai.core.embedding.metadata.EmbeddingResponseMetadata
import org.krosai.core.embedding.metadata.MetadataMode
import org.krosai.core.embedding.model.*
import org.krosai.openai.api.OpenAiApi
import org.krosai.openai.api.embedding.OpenAiEmbeddingRequest
import org.krosai.openai.metadata.OpenAiUsage
import org.krosai.openai.options.OpenAiEmbeddingOptions

class OpenAiEmbeddingModel(
    val api: OpenAiApi,
    val embeddingOptions: OpenAiEmbeddingOptions = OpenAiEmbeddingOptions("text-embedding-ada-002"),
    val metadataMode: MetadataMode = MetadataMode.EMBED
): EmbeddingModel {
    override suspend fun call(request: EmbeddingRequest): EmbeddingResponse {
        val options = mergeOptions(request.options)
        val apiRequest = createRequest(request, options)
        val response = api.embeddings(apiRequest)

        val metadata = EmbeddingResponseMetadata(
            response.model,
            OpenAiUsage(response.usage)
        )

        val embeddings = response.data.map {
            Embedding(
                it.output,
                it.index
            )
        }

        return EmbeddingResponse(embeddings, metadata)

    }

    private fun createRequest(
        request: EmbeddingRequest,
        requestOptions: OpenAiEmbeddingOptions
    ): OpenAiEmbeddingRequest<List<String>> {
        return OpenAiEmbeddingRequest(
            request.instructions,
            requestOptions.model,
            requestOptions.encodingFormat.orEmpty(),
            requestOptions.dimension,
            requestOptions.user
        )
    }

    private fun mergeOptions(runtimeOptions: EmbeddingOptions): OpenAiEmbeddingOptions {
        return OpenAiEmbeddingOptions(
            model = runtimeOptions.model ?: embeddingOptions.model,
            dimension = runtimeOptions.dimension ?: embeddingOptions.dimension,
            encodingFormat = embeddingOptions.encodingFormat,
            user = embeddingOptions.user
        )
    }
}