package io.kamo.ktor.client.ai.test

import io.kamo.ktor.client.ai.core.chat
import io.kamo.ktor.client.ai.openai.config.OpenAi
import io.ktor.client.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.runBlocking

fun main() = run {

    val client = HttpClient {
        install(ContentNegotiation){
            json()
        }
        install(OpenAi) {
            apiKey = System.getenv("apiKey")

            chatOptions{
                baseUrl = "https://www.jcapikey.com"
                model = "gpt-4o"
            }
            embeddingOptions {
                baseUrl = "https://www.jcapikey.com"
                model = "gpt-4o"
            }
        }
    }
    runBlocking {
        println(client.chat("你好"))
    }

}