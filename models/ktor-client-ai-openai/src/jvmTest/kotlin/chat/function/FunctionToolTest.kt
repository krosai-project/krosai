package io.kamo.ktor.client.ai.openai.test.chat.function

import io.kamo.ktor.client.ai.core.chat.function.buildFunctionCall
import io.kamo.ktor.client.ai.core.factory.buildModelFactoryContext
import io.kamo.ktor.client.ai.core.util.DefaultJsonConverter
import io.kamo.ktor.client.ai.core.util.SerialDescription
import io.kamo.ktor.client.ai.openai.factory.OpenAI
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.plugins.sse.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.LocalDateTime
import kotlin.test.Test


class FunctionToolTest {

    private val context = buildModelFactoryContext {
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
            baseUrl = "https://www.jcapikey.com"
            apiKey = System.getenv("apiKey").orEmpty()
        }
    }[OpenAI]

    private val chatClient = context.createChatClient()

    @Test
    fun functionCallCallTest() = runTest {
        chatClient.call {
            userText = { "现在湖南是什么时间?" }
            functions {
                functionCalls.add(dateTimeFun)
            }
        }.let {
            println(it.result.output.content)
        }
    }

    @Test
    fun functionCallStreamTest() = runTest {
        chatClient.stream {
            userText = { "现在湖南是什么时间?" }
            functions {
                functionCalls.add(dateTimeFun)
            }
        }.collect {
            println(it.result.output.content)
        }
    }
}


@SerialDescription("get date time API Request")
@Serializable
data class Request(
    @SerialName("location") @SerialDescription("地区")
    val location: String
)

val dateTimeFun = buildFunctionCall {
    name = "date_time"
    description = "根据地区,获取当前日期"
    withCall { req: Request ->
        "${req.location}的日期是${LocalDateTime.now()}"
    }
}