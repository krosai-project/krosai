package io.github.krosai.samples

interface Platform {
    val name: String
    var context: Any?
}

expect fun getPlatform(): Platform