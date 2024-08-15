package org.krosai.core.chat.function

import kotlinx.serialization.json.JsonElement

/**
 * Represents a function call that can be invoked with an input and returns an output.
 *
 * @author KAMOsama
 */
interface FunctionCall<in I, out O> {

    val name: String

    val description: String

    val inputSchema: JsonElement

    fun call(req: String): String

    operator fun invoke(req: I): O

}