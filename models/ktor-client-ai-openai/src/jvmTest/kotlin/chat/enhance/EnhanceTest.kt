package io.kamo.ktor.client.ai.openai.test.chat.enhance

import io.kamo.ktor.client.ai.core.chat.enhancer.ChatMemorySupport
import io.kamo.ktor.client.ai.core.chat.enhancer.MessageChatMemoryEnhancer
import io.kamo.ktor.client.ai.core.chat.memory.InMemoryMessageStore
import io.kamo.ktor.client.ai.openai.test.ModelFactorySupport
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

class EnhanceTest : ModelFactorySupport {

    @Test
    fun enhanceCallTest() = runTest {
        val chatClient = context.createChatClient {
            enhancers {
                +MessageChatMemoryEnhancer(InMemoryMessageStore())
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
                +MessageChatMemoryEnhancer(InMemoryMessageStore())
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
            assert(it.content.contains("Kamo"))
        }

    }

    @Test
    fun enhanceParamsTest() = runTest {
        val chatClient = context.createChatClient {
            enhancers {
                +MessageChatMemoryEnhancer(InMemoryMessageStore())
            }
        }
        chatClient.call {
            userText = { "Please call me Kamo." }
            enhancers {
                ChatMemorySupport.CONVERSATION_ID_KEY to "test"
            }
        }.let {
            println("content: ${it.content}")
        }


        chatClient.call {
            userText = { "Who am I" }
            enhancers {
                // if you want to use default conversation id, you can use this.
                ChatMemorySupport.CONVERSATION_ID_KEY to "test"
            }
        }.let {
            println("content: ${it.content}")
            assert(it.content.contains("Kamo"))
        }

    }

}