package org.krosai.core.image

import org.krosai.core.model.ModelResult

class ImageGeneration(
    override val output: Image,
    override val metadata: ImageGenerationMetadata = ImageGenerationMetadata.NULL
) : ModelResult<Image> {

}