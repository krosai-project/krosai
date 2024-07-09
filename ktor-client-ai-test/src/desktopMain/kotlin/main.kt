package io.kamo.ktor.client.ai.test

//import io.kamo.ktor.client.ai.core.steaming
import io.kamo.ktor.client.ai.core.chat
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

    runBlocking {

        client.chat.request{

            userText = "你是谁"

            system {
                text = "你是一名{role}请回答{target}的问题"
                params("role" to "医生", "target" to "客户")
            }

        }.stream().collect{
            println(it.result.output.content)
        }

    }

}