package io.github.krosai.openai.model

import io.github.krosai.core.embedding.metadata.EmbeddingResponseMetadata
import io.github.krosai.core.embedding.metadata.MetadataMode
import io.github.krosai.core.embedding.model.Embedding
import io.github.krosai.core.embedding.model.EmbeddingModel
import io.github.krosai.core.embedding.model.EmbeddingOptions
import io.github.krosai.core.embedding.model.EmbeddingRequest
import io.github.krosai.core.embedding.model.EmbeddingResponse
import io.github.krosai.openai.api.OpenAiApi
import io.github.krosai.openai.metadata.OpenAiUsage
import io.github.krosai.openai.options.OpenAiEmbeddingOptions

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
    ): io.github.krosai.openai.api.EmbeddingRequest<List<String>> {
        return io.github.krosai.openai.api.EmbeddingRequest(
            request.instructions,
            requestOptions.model.orEmpty(),
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