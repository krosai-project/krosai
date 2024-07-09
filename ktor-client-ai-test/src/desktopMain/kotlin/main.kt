package io.kamo.ktor.client.ai.test

//import io.kamo.ktor.client.ai.core.steaming
import io.kamo.ktor.client.ai.core.Prompt
import io.kamo.ktor.client.ai.core.message.Message
import io.kamo.ktor.client.ai.core.steaming
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
        install(SSE){
//            reconnectionTime = (-1).milliseconds
        }
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
        val prompt = Prompt(
            Message.System("你是一名医生请回答患者的问题"),
            Message.User("你是谁")
        )
        client.steaming(prompt).collect{
            println(it.result.output.content)
        }

    }



}