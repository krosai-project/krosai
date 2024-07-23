package io.kamo.ktor.client.ai.core.chat.function

interface FunctionCallOptions {

    val functionCalls: List<FunctionCall>

    val functionNames: List<String>
}
