package io.github.krosai.core.chat.function

interface FunctionCallOptions {

    val functionCalls: MutableList<FunctionCall>

    val functionNames: MutableSet<String>

}
