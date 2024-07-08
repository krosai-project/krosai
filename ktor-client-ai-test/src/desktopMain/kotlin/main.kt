package io.kamo.ktor.client.ai.test

import io.kamo.ktor.client.ai.core.chat
import io.kamo.ktor.client.ai.core.steaming
import io.kamo.ktor.client.ai.openai.config.OpenAi
import io.ktor.client.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*

suspend fun main() = run {

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
    client.steaming("你好").collect{
        println(it.result)
    }
    client.chat("你好").let {
        println(it.result)
    }


}