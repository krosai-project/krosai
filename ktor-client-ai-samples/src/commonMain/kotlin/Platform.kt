package io.kamo.ktor.client.ai.samples

interface Platform {
    val name: String
    var context: Any?
}

expect fun getPlatform(): Platform