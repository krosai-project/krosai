package org.krosai.core.image

import org.krosai.core.model.ModelOptions

interface ImageOptions : ModelOptions {

    val n: Int?

    val model: String?

    val width: Int?

    val height: Int?

    /**
     * openai - url or base64 : stability ai byte[] or base64
     */
    val responseFormat: String?

    val style: String?

}