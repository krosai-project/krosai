package io.github.krosai.core.image

import io.github.krosai.core.model.ModelOptions

interface ImageOptions : ModelOptions {

    val n: Int?

    val model: String?

    val width: Int?

    val height: Int?

    /**
     * openai - url or base64 : stability ai byte[] or base64
     */
    val responseFormat: String?

}