package org.krosai.core.image

data class DefaultImageOptions(
    override val n: Int? = null,
    override val model: String? = null,
    override val width: Int? = null,
    override val height: Int? = null,
    override val responseFormat: String? = null,
    override val style: String? = null,
) : ImageOptions