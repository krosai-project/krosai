package org.krosai.openai.test

import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.plugins.sse.*
import io.ktor.serialization.kotlinx.json.*
import org.krosai.core.chat.client.ChatClient
import org.krosai.core.factory.ModelFactory
import org.krosai.core.factory.buildModelFactoryContext
import org.krosai.core.util.DefaultJsonConverter
import org.krosai.openai.api.image.OpenAiImageModelEnum
import org.krosai.openai.factory.OpenAi

interface ModelFactorySupport {

    val factory: ModelFactory
        get() {
            return buildModelFactoryContext {
                factory(OpenAi) {
                    clientBlock = {
                        install(HttpTimeout) {
                            requestTimeoutMillis = 60000
                            connectTimeoutMillis = 60000
                            socketTimeoutMillis = 60000
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
            }[OpenAi]
        }
    val chatClient: ChatClient
        get() = factory.createChatClient()

}