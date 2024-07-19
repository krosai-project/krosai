package io.kamo.ktor.client.ai.test

import io.kamo.ktor.client.ai.core.chat.client.createChatClient
import io.kamo.ktor.client.ai.openai.config.OpenAi
import io.ktor.client.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.plugins.sse.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.runBlocking

fun main(): Unit = run {

    val client = HttpClient {
        install(ContentNegotiation){
            json()
        }
        install(SSE)
        install(Logging) {
            logger = Logger.SIMPLE
            this.level = LogLevel.ALL
        }
        install(OpenAi) {
            apiKey = System.getenv("apiKey")

            chatOptions{
                baseUrl = "https://www.jcapikey.com"
                model = "gpt-3.5-turbo"
            }
            embeddingOptions {
                baseUrl = "https://www.jcapikey.com"
                model = "gpt-4o"
            }
        }
    }
    // 构建
    val chatClient = client.createChatClient {
        systemText = { "你是一名${get("role")}请回答${get("target")}的问题" }
    }
    runBlocking {
        chatClient.call {
            userText = { "你是谁" }
            system {
                "role" to "专业的医生"
                "target" to "客户"
            }
        }.let {
            println(it.result.output.content)
        }
        chatClient.stream {
            userText = { "你是谁" }
            system {
                "role" to "智能客服"
                "target" to "客户"
            }

        }.collect{
            println(it.result.output.content)
        }
    }
}



