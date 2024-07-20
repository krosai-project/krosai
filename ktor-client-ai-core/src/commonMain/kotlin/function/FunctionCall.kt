package io.kamo.ktor.client.ai.core.function

interface FunctionCall {
    val name: String

    val description: String

    val inputTypeSchema: String

    fun call(req: String): String
}