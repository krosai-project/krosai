package io.github.krosai.openai.test.image

import io.github.krosai.core.image.ImagePrompt
import io.github.krosai.openai.options.OpenAiImageOptions
import io.github.krosai.openai.test.ModelFactorySupport
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

class ImageTest : ModelFactorySupport {

    @Test
    fun imageModelTest() = runTest {

        val imageModel = factory.createImageModel()
        imageModel.call(
            ImagePrompt(
                instructions = "A light cream colored mini golden doodle",
                options = OpenAiImageOptions(
                    quality = "hd",
                    n = 4,
                    height = 1024,
                    width = 1024
                )
            )
        ).let {
            println(it.result)
        }

    }
}