package io.github.krosai.openai.test

import io.github.krosai.core.chat.client.ChatClient
import io.github.krosai.core.factory.ModelFactory
import io.github.krosai.core.factory.buildModelFactoryContext
import io.github.krosai.core.util.DefaultJsonConverter
import io.github.krosai.openai.api.image.OpenAiImageModelEnum
import io.github.krosai.openai.factory.OpenAI
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.plugins.sse.*
import io.ktor.serialization.kotlinx.json.*

interface ModelFactorySupport {

    val factory: ModelFactory
        get() {
            return buildModelFactoryContext {
                factory(OpenAI) {
                    clientBlock = {
                        install(HttpTimeout) {
                            requestTimeoutMillis = 600000
                            connectTimeoutMillis = 600000
                            socketTimeoutMillis = 600000
                        }
                        install(ContentNegotiation) {
                            json(DefaultJsonConverter)
                        }
                        install(SSE)
                        install(Logging) {
                            level = LogLevel.ALL
                            logger = Logger.SIMPLE
                        }
                    }
                    baseUrl = LocalData.BASE_URL
                    apiKey = LocalData.API_KEY
//                    chatOptions {
//                        model = "GLM-4-0520"
//                    }
                    embeddingOptions {
                        model = "text-embedding-ada-002"
                        encodingFormat = "float"
                    }
                    imageOptions {
                        model = OpenAiImageModelEnum.DALL_E_3.model
                    }
                }
            }[OpenAI]
        }
    val chatClient: ChatClient
        get() = factory.createChatClient()

}