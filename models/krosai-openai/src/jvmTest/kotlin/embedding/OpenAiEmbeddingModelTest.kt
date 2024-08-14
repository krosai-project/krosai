package org.krosai.openai.test.embedding

import kotlinx.coroutines.test.runTest
import org.krosai.core.embedding.model.EmbeddingRequest
import org.krosai.openai.model.OpenAiEmbeddingModel
import org.krosai.openai.test.ModelFactorySupport
import kotlin.test.Test

class OpenAiEmbeddingModelTest : ModelFactorySupport {

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