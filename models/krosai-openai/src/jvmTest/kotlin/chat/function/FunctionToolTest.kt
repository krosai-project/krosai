package io.github.krosai.openai.test.chat.function

import io.github.krosai.core.chat.function.buildFunctionCall
import io.github.krosai.core.util.SerialDescription
import io.github.krosai.openai.test.ModelFactorySupport
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.LocalDateTime
import kotlin.test.Test


class FunctionToolTest : ModelFactorySupport {
    @Test
    fun functionCallCallTest() = runTest {
        chatClient.call {
            userText = { "Get the current date by location?" }
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
            userText = { "Get the current date by location?" }
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
    @SerialName("location")
    @SerialDescription("location")
    val location: String
)
val dateTimeFun = buildFunctionCall {
    name = "date_time"
    description = "Get the current date by location"
    withCall { req: Request ->
        "the current time in ${req.location} is ${LocalDateTime.now()}"
    }
}
