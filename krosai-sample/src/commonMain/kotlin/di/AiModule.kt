package org.krosai.sample.di

import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.plugins.sse.*
import io.ktor.serialization.kotlinx.json.*
import org.koin.core.qualifier._q
import org.koin.dsl.bind
import org.koin.dsl.module
import org.krosai.core.chat.client.ChatClient
import org.krosai.core.chat.enhancer.Enhancer
import org.krosai.core.chat.enhancer.MessageChatMemoryEnhancer
import org.krosai.core.chat.function.FunctionCall
import org.krosai.core.chat.memory.InMemoryMessageStore
import org.krosai.core.factory.ModelFactory
import org.krosai.core.factory.ModelFactoryContext
import org.krosai.core.util.DefaultJsonConverter
import org.krosai.openai.factory.OpenAi
import org.krosai.sample.LocalData
import org.krosai.sample.function.GetURL
import org.krosai.sample.function.OpenBrowser

val AiModule = module {

    single<ModelFactory> {
        ModelFactoryContext()
            .create(OpenAi) {
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


