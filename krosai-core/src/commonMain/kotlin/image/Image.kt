package org.krosai.core.image

data class Image(

    /**
     * The URL where the image can be accessed.
     */
    val url: String,

    /**
     * The base64 encoded image.
     */
    val b64Json: String,

    )