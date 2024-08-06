package io.kamo.ktor.client.ai.openai.test.chat.function

import io.kamo.ktor.client.ai.core.chat.function.buildFunctionCall
import io.kamo.ktor.client.ai.core.util.SerialDescription
import io.kamo.ktor.client.ai.openai.test.ModelFactorySupport
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.LocalDateTime
import kotlin.test.Test


class FunctionToolTest : ModelFactorySupport {
    @Test
    fun functionCallCallTest() = runTest {
        chatClient.call {
            userText = { "现在湖南是什么时间?" }
            functions {
                +dateTimeFun
            }
        }.let {
            println(it.content)
        }
    }

    @Test
    fun functionCallStreamTest() = runTest {
        chatClient.stream {
            userText = { "现在湖南是什么时间?" }
            functions {
                +dateTimeFun
            }
        }.collect {
            println(it.content)
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

