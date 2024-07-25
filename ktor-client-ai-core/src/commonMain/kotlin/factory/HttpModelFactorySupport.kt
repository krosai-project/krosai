package io.kamo.ktor.client.ai.core.factory

import io.ktor.client.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.sse.*
import io.ktor.serialization.kotlinx.json.*

interface HttpModelFactorySupport {
    fun createDefaultHttpClient() = HttpClient {
        install(ContentNegotiation) {
            json()
        }
        install(SSE)
    }

}