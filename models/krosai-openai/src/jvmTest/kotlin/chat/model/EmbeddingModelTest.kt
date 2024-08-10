package io.github.krosai.openai.test.chat.model

import io.github.krosai.core.embedding.model.EmbeddingRequest
import io.github.krosai.openai.model.OpenAiEmbeddingModel
import io.github.krosai.openai.test.ModelFactorySupport
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

class EmbeddingModelTest: ModelFactorySupport {

    @Test
    fun testEmbeddingModel() {
        runTest {
            val embeddingModel = factory.createEmbeddingModel() as OpenAiEmbeddingModel

            val request = EmbeddingRequest(
                instructions = listOf("Hello, world!"),
                options = embeddingModel.embeddingOptions
            )

            val response = embeddingModel.call(request)

            println(response)
        }
    }
}