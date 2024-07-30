package io.kamo.ktor.client.ai.core.chat.function

interface FunctionCall {

    val name: String

    val description: String

    val inputSchema: String

    fun call(req: String): String

}