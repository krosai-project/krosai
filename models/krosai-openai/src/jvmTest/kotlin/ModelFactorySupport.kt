package io.github.krosai.openai.test

import io.github.krosai.core.chat.client.ChatClient
import io.github.krosai.core.factory.ModelFactory
import io.github.krosai.core.factory.buildModelFactoryContext
import io.github.krosai.core.util.DefaultJsonConverter
import io.github.krosai.openai.factory.OpenAI
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.plugins.sse.*
import io.ktor.serialization.kotlinx.json.*

interface ModelFactorySupport {

    val context: ModelFactory
        get() {
            return buildModelFactoryContext {
                factory(OpenAI) {
                    clientBlock = {
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
                }
            }[OpenAI]
        }
    val chatClient: ChatClient
        get() = context.createChatClient()

}