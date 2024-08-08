package io.github.krosai.core.chat.function

import kotlinx.serialization.json.JsonElement

/**
 * Represents an abstract function call that takes an input of type [I] and returns an output of type [O].
 * The function call is defined by the [name], [description], [inputSchema], [inputConverter], and [outputConverter].
 * This class implements the [FunctionCall] interface and also acts as a [Func1] function.
 *
 * @param I The input type of the function call.
 * @param O The output type of the function call.
 * @param name The name of the function call.
 * @param description The description of the function call.
 * @param inputSchema The input schema of the function call as a JSON element.
 * @param inputConverter The converter function to convert the input string to the input type [I].
 * @param outputConverter The converter function to convert the output type [O] to a string.
 *
 * @see FunctionCall
 * @see Func1
 *
 * @author KAMOsama
 */
abstract class AbstractFunctionCall<in I : Any, out O : Any>(
    override val name: String,
    override val description: String,
    override val inputSchema: JsonElement,
    private val inputConverter: Func1<String, I>,
    private val outputConverter: Func1<O, String>,
) : FunctionCall, Func1<I, O> {

    override fun call(req: String): String =
        inputConverter(req).let(::invoke).let(outputConverter)

}

typealias Func1<I, O> = (I) -> O
