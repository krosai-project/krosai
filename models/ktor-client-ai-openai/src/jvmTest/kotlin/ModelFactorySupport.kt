package io.kamo.ktor.client.ai.openai.test

import io.kamo.ktor.client.ai.core.chat.client.ChatClient
import io.kamo.ktor.client.ai.core.factory.ModelFactory
import io.kamo.ktor.client.ai.core.factory.buildModelFactoryContext
import io.kamo.ktor.client.ai.core.util.DefaultJsonConverter
import io.kamo.ktor.client.ai.openai.factory.OpenAI
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