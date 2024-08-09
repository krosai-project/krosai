package io.github.krosai.core.image

import io.github.krosai.core.model.ModelResult

class ImageGeneration(
    override val output: Image,
    override val metadata: ImageGenerationMetadata = ImageGenerationMetadata.NULL
) : ModelResult<Image> {

}