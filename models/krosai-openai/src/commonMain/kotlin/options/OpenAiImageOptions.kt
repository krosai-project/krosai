package org.krosai.openai.options

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.krosai.core.image.ImageOptions
import org.krosai.openai.api.image.OpenAiImageModelEnum

@Serializable
data class OpenAiImageOptions(

    /**
     * The number of images to generate. Must be between 1 and 10. For dall-e-3, only n=1
     * is supported.
     */
    @SerialName("n")
    override var n: Int? = null,

    /**
     * The model to use for image generation.
     */
    @SerialName("model")
    override var model: String = OpenAiImageModelEnum.DEFAULT.model,

    /**
     * The width of the generated images. Must be one of 256, 512, or 1024 for dall-e-2.
     */
    @SerialName("size_width")
    override var width: Int = 1024,

    /**
     * The height of the generated images. Must be one of 256, 512, or 1024 for dall-e-2.
     */
    @SerialName("size_height")
    override var height: Int = 1024,

    /**
     * The size of the generated images. Must be one of 256x256, 512x512, or 1024x1024 for
     * dall-e-2. Must be one of 1024x1024, 1792x1024, or 1024x1792 for dall-e-3 models.
     */
    @SerialName("size")
    var size: String = "${width}x${height}",

    /**
     * The quality of the image that will be generated. hd creates images with finer
     * details and greater consistency across the image. This param is only supported for
     * dall-e-3.
     */
    @SerialName("quality")
    var quality: String? = null,

    /**
     * The format in which the generated images are returned. Must be one of url or
     * b64_json.
     */
    @SerialName("response_format")
    override var responseFormat: String? = null,

    /**
     * The style of the generated images. Must be one of vivid or natural. Vivid causes
     * the model to lean towards generating hyper-real and dramatic images. Natural causes
     * the model to produce more natural, less hyper-real looking images. This param is
     * only supported for dall-e-3.
     */
    @SerialName("style")
    override var style: String? = null,

    /**
     * A unique identifier representing your end-user, which can help OpenAI to monitor
     * and detect abuse.
     */
    @SerialName("user")
    var user: String? = null
) : ImageOptions