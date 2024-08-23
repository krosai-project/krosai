package org.krosai.openai.test.chat.function

import kotlinx.coroutines.test.runTest
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.krosai.core.chat.function.buildFunctionCall
import org.krosai.core.util.SerialDescription
import org.krosai.openai.test.ModelFactorySupport
import java.time.LocalDateTime
import kotlin.test.Test


class FunctionToolTest : ModelFactorySupport {
    @Test
    fun functionCallCallTest() = runTest {
        chatClient.call {
            userText { "Get the current date on New York?" }
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
            userText { "Get the current date on New York?" }
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
