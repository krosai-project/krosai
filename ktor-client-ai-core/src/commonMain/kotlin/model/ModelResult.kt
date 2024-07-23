package io.kamo.ktor.client.ai.core.model

interface ModelResult<T> {

    /**
     * Retrieves the output generated by the AI model.
     * @return the output generated by the AI model
     */
    val output: T

    /**
     * Retrieves the metadata associated with the result of an AI model.
     * @return the metadata associated with the result
     */
    val resultMetadata: ResultMetadata

}

interface ResultMetadata{

    object NULL : ResultMetadata

}
