package io.kamo.ktor.client.ai.samples.di

import io.kamo.ktor.client.ai.core.chat.client.ChatClient
import io.kamo.ktor.client.ai.core.chat.enhancer.Enhancer
import io.kamo.ktor.client.ai.core.chat.enhancer.MessageChatMemoryEnhancer
import io.kamo.ktor.client.ai.core.chat.function.FunctionCall
import io.kamo.ktor.client.ai.core.chat.memory.InMemoryMessageStore
import io.kamo.ktor.client.ai.core.factory.ModelFactory
import io.kamo.ktor.client.ai.core.factory.buildModelFactoryContext
import io.kamo.ktor.client.ai.core.util.DefaultJsonConverter
import io.kamo.ktor.client.ai.openai.factory.OpenAI
import io.kamo.ktor.client.ai.samples.LocalData
import io.kamo.ktor.client.ai.samples.function.GetURL
import io.kamo.ktor.client.ai.samples.function.OpenBrowser
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.plugins.sse.*
import io.ktor.serialization.kotlinx.json.*
import org.koin.core.qualifier._q
import org.koin.dsl.bind
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
                +getAll<FunctionCall>()
            }
            enhancers {
                +getAll<Enhancer>()
            }
        }
    }

    single(_q("openBrowserURL")) {
        OpenBrowser
    } bind FunctionCall::class

    single(_q("getURL")) {
        GetURL
    } bind FunctionCall::class

    single {
        MessageChatMemoryEnhancer(InMemoryMessageStore())
    } bind Enhancer::class


}


