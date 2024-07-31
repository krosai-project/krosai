package io.kamo.ktor.client.ai.test

import io.kamo.ktor.client.ai.core.chat.client.function
import io.kamo.ktor.client.ai.core.chat.function.FunctionCall
import io.kamo.ktor.client.ai.core.factory.buildModelFactoryContext
import io.kamo.ktor.client.ai.openai.factory.OpenAI
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull

fun main(): Unit = run {
    val context = buildModelFactoryContext {
        factory(OpenAI) {
            baseUrl = "https://www.jcapikey.com"
            apiKey = System.getenv("apiKey")
        }
    }
    val chatClient = context[OpenAI].createChatClient {
        functions {
            function<String, String>("date", "获取当前日期") { "2022-12-12" }
        }
    }
//    val client = HttpClient {
//        install(ContentNegotiation){
//            json()
//        }
//        install(SSE)
//        install(Logging) {
//            logger = Logger.SIMPLE
//            this.level = LogLevel.ALL
//        }
//        install(OpenAi) {
//            apiKey = System.getenv("apiKey")
//
//            chatOptions{
//                baseUrl = "https://www.jcapikey.com"
//                model = "gpt-3.5-turbo"
//            }
//            embeddingOptions {
//                baseUrl = "https://www.jcapikey.com"
//                model = "gpt-4o"
//            }
//        }
//    }
//    // 构建
//    val chatClient = client.createChatClient {
//        systemText = { "你是一名${get("role")}请回答${get("target")}的问题" }
//    }
    runBlocking {
        chatClient.call {
            userText = { "你是谁" }
            systemText = { "你是一名${get("role")}请回答${get("target")}的问题" }

            system {
                "role" to "专业的医生"
                "target" to "客户"
            }
        }.let {
            println(it.result.output.content)
        }
        chatClient.call {
            userText = { "现在时间是多少" }

        }.let {
            println(it.result.output.content)
        }
//        chatClient.stream {
//            userText = { "你是谁" }
//            system {
//                "role" to "智能客服"
//                "target" to "客户"
//            }
//
//        }.collect {
//            println(it.result.output.content)
//        }
    }
}

class DateFunctionInClass : FunctionCall {
    override val name: String = "date"
    override val description: String = "获取当前日期"
    override val inputSchema: JsonElement = JsonNull
    override fun call(req: String): String {
        return "2022-12-12"
    }
}

fun dateFunction(): String {
    return "2022-12-12"
}