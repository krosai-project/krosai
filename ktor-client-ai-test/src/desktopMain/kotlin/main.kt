package io.kamo.ktor.client.ai.test

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.*

fun main(): Unit = run {


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
//    runBlocking {
//        chatClient.call {
//            userText = { "你是谁" }
//            system {
//                "role" to "专业的医生"
//                "target" to "客户"
//            }
//        }.let {
//            println(it.result.output.content)
//        }
//        chatClient.stream {
//            userText = { "你是谁" }
//            system {
//                "role" to "智能客服"
//                "target" to "客户"
//            }
//
//        }.collect{
//            println(it.result.output.content)
//        }
//    }
}


annotation class AiFunction {

}

@Serializable
data class Student(val name: String, val age: Int)

@AiFunction
fun tag(name: String, age: Int, student: Student) {
    println("$name is $age years old; $student")
}

@AiFunction
fun tag2(vararg student: Student) {
    student.forEach { println(it) }
}

fun test() {
    registerFun(::tag.name, "xxx") { it: Map<String, JsonElement> ->
        tag(
            name = it["name"]!!.jsonPrimitive.content,
            age = it["age"]!!.jsonPrimitive.int,
            student = Json.decodeFromJsonElement(it["student"]!!)
        )
    }
}

inline fun <reified I : Any, reified O : Any> registerFun(
    name: String,
    descriptor: String,
    call: (I) -> O
) {

}
