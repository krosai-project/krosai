package io.github.krosai.client.ai.core.chat.function

import kotlinx.serialization.json.JsonElement

interface FunctionCall {

    val name: String

    val description: String

    val inputSchema: JsonElement

    fun call(req: String): String

}