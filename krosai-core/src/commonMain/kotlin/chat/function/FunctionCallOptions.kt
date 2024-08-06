package io.github.krosai.client.ai.core.chat.function

interface FunctionCallOptions {

    val functionCalls: MutableList<FunctionCall>

    val functionNames: MutableSet<String>

}
