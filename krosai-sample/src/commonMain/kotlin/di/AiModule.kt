package io.github.krosai.sample.di

import io.github.krosai.core.chat.client.ChatClient
import io.github.krosai.core.chat.enhancer.Enhancer
import io.github.krosai.core.chat.enhancer.MessageChatMemoryEnhancer
import io.github.krosai.core.chat.function.FunctionCall
import io.github.krosai.core.chat.memory.InMemoryMessageStore
import io.github.krosai.core.factory.ModelFactory
import io.github.krosai.core.factory.buildModelFactoryContext
import io.github.krosai.core.util.DefaultJsonConverter
import io.github.krosai.openai.factory.OpenAi
import io.github.krosai.sample.LocalData
import io.github.krosai.sample.function.GetURL
import io.github.krosai.sample.function.OpenBrowser
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.plugins.sse.*
import io.ktor.serialization.kotlinx.json.*
import org.koin.core.qualifier._q
import org.koin.dsl.bind
import org.koin.dsl.module

val AiModule = module {
    single<ModelFactory> {
        buildModelFactoryContext {
            factory(OpenAi) {
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
        }[OpenAi]
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


