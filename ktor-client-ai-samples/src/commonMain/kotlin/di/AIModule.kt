package io.kamo.ktor.client.ai.samples.di

import io.kamo.ktor.client.ai.core.chat.client.ChatClient
import io.kamo.ktor.client.ai.core.chat.function.FunctionCall
import io.kamo.ktor.client.ai.core.factory.ModelFactory
import io.kamo.ktor.client.ai.core.factory.buildModelFactoryContext
import io.kamo.ktor.client.ai.core.util.DefaultJsonConverter
import io.kamo.ktor.client.ai.openai.factory.OpenAI
import io.kamo.ktor.client.ai.samples.LocalData
import io.kamo.ktor.client.ai.samples.function.OpenBrowser
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.plugins.sse.*
import io.ktor.serialization.kotlinx.json.*
import org.koin.dsl.module

val AIModule = module {
    single<ModelFactory> {
        buildModelFactoryContext {
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


    single<ChatClient> {
        get<ModelFactory>().createChatClient {
            functions {
                functionCalls += getAll()
            }
        }
    }

    single<FunctionCall> {
        OpenBrowser
    }

}


