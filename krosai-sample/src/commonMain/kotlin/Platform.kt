package io.github.krosai.client.ai.samples

interface Platform {
    val name: String
    var context: Any?
}

expect fun getPlatform(): Platform