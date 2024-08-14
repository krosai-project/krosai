package org.krosai.openai.test.image

import kotlinx.coroutines.test.runTest
import org.krosai.core.image.ImagePrompt
import org.krosai.openai.options.OpenAiImageOptions
import org.krosai.openai.test.ModelFactorySupport
import kotlin.test.Test

class ImageTest : ModelFactorySupport {

    @Test
    fun `image model test`() = runTest {

        val imageModel = factory.createImageModel()
        imageModel.call(
            ImagePrompt(
                instructions = "A light cream colored mini golden doodle",
                options = OpenAiImageOptions(
                    model = "dall-e-2",
                    quality = "hd",
                    n = 1,
                    height = 256,
                    width = 256
                )
            )
        ).let {
            println(it.result)
        }

    }
}