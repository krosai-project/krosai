package io.github.krosai.samples.di

import io.github.krosai.core.chat.client.ChatClient
import io.github.krosai.core.chat.enhancer.Enhancer
import io.github.krosai.core.chat.enhancer.MessageChatMemoryEnhancer
import io.github.krosai.core.chat.function.FunctionCall
import io.github.krosai.core.chat.memory.InMemoryMessageStore
import io.github.krosai.core.factory.ModelFactory
import io.github.krosai.core.factory.buildModelFactoryContext
import io.github.krosai.core.util.DefaultJsonConverter
import io.github.krosai.openai.factory.OpenAI
import io.github.krosai.samples.LocalData
import io.github.krosai.samples.function.GetURL
import io.github.krosai.samples.function.OpenBrowser
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


