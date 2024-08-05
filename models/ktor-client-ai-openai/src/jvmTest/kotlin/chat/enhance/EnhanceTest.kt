package io.kamo.ktor.client.ai.openai.test.chat.enhance

import io.kamo.ktor.client.ai.core.chat.client.MessageMemoryEnhance
import io.kamo.ktor.client.ai.openai.test.ModelFactorySupport
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

class EnhanceTest : ModelFactorySupport {

    @Test
    fun enhanceCallTest() = runTest {
        val chatClient = context.createChatClient {
            enhancers {
                +MessageMemoryEnhance()
            }
        }
        chatClient.call {
            userText = { "Please call me Kamo." }
        }.let {
            println("content: ${it.content}")
        }


        chatClient.call {
            userText = { "Please answer in Japanese" }
        }.let {
            println("content: ${it.content}")
        }
    }

    @Test
    fun enhanceStreamTest() = runTest {
        val chatClient = context.createChatClient {
            enhancers {
                +MessageMemoryEnhance()
            }
        }
        chatClient.stream {
            userText = { "Please call me Kamo." }
        }.collect {
            println("content: ${it.content}")
        }


        chatClient.stream {
            userText = { "Please answer in Japanese" }
        }.collect {
            println("content: ${it.content}")
        }

    }

}
