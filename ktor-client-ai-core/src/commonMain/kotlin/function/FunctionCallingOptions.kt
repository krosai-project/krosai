package io.kamo.ktor.client.ai.core.function

interface FunctionCallingOptions {

    val functionCalls: List<FunctionCall>

    val functionNames: List<String>
}
