package io.github.krosai.openai.test.chat

import io.github.krosai.core.chat.message.Media
import io.github.krosai.core.chat.message.Message
import io.github.krosai.core.chat.prompt.Prompt
import io.github.krosai.openai.test.ModelFactorySupport
import io.ktor.http.*
import kotlinx.coroutines.test.runTest
import org.junit.Test
import java.io.File

class ChatTest : ModelFactorySupport {

    @Test
    fun `test image media`() = runTest {
        val chatModel = factory.createChatModel()
        val messages = listOf(
            Message.User(
                content = "What is this image?",
                media = listOf(
//                    Media.Url(
//                        ContentType.Image.JPEG,
//                        "https://upload.wikimedia.org/wikipedia/commons/thumb/d/dd/Gfp-wisconsin-madison-the-nature-boardwalk.jpg/2560px-Gfp-wisconsin-madison-the-nature-boardwalk.jpg"
//                    ),
                    Media.Bytes(
                        ContentType.Image.PNG,
                        File("src/jvmTest/resources/img_8.png").readBytes()
                    )

                )
            )
        )
        chatModel.call(
            Prompt(
                messages,
                chatModel.defaultChatOptions
            )
        ).let {
            println(it.content)
        }

    }

    @Test
    fun `test image media with prompt`() = runTest {
        println("  1".toInt())
    }

}