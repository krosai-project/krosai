package org.krosai.core.image

import org.krosai.core.model.ModelRequest

data class ImagePrompt(
    override val instructions: List<ImageMessage> = emptyList(),
    override var options: ImageOptions,
) : ModelRequest<List<ImageMessage>> {

    constructor(imageMessage: ImageMessage, options: ImageOptions) : this(
        instructions = listOf(imageMessage),
        options = options
    )

    constructor(instructions: String, options: ImageOptions = DefaultImageOptions()) : this(
        instructions = listOf(ImageMessage(text = instructions)),
        options = options
    )

}