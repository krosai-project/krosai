package io.github.krosai.openai.api.embedding

/**
 * OpenAI Embeddings Models:
 * [Embeddings](https://platform.openai.com/docs/models/embeddings)
 */
enum class OpenAiEmbeddingModelEnum(val model: String) {

    /**
     * Most capable embedding model for both english and non-english tasks. DIMENSION:
     * 3072
     */
    TEXT_EMBEDDING_3_LARGE("text-embedding-3-large"),

    /**
     * Increased performance over 2nd generation ada embedding model. DIMENSION: 1536
     */
    TEXT_EMBEDDING_3_SMALL("text-embedding-3-small"),

    /**
     * Most capable 2nd generation embedding model, replacing 16 first generation
     * models. DIMENSION: 1536
     */
    TEXT_EMBEDDING_ADA_002("text-embedding-ada-002");

    companion object {
        val DEFAULT = TEXT_EMBEDDING_ADA_002
    }

}