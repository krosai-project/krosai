package io.kamo.ktor.client.ai.test

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform