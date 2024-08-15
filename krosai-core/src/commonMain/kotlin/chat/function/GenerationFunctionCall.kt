package org.krosai.core.chat.function

import kotlinx.serialization.json.JsonElement

/**
 * Represents a function call that can be invoked with an input and returns an output.
 *
 * @property name The name of the function call.
 * @property description The description of the function call.
 * @property inputSchema The input schema of the function call in JSON format.
 * @property inputConverter A function used to convert a string input to the appropriate type.
 * @property outputConverter A function used to convert the output to a string.
 * @property doCall The function called when the function call is invoked.
 * @param I The input type of the function call.
 * @param O The output type of the function call.
 */
class GenerationFunctionCall<in I, out O>(
    override val name: String,
    override val description: String,
    override val inputSchema: JsonElement,
    private val inputConverter: Func1<String, I>,
    private val outputConverter: Func1<O, String>,
    private val doCall: Func1<I, O>
) : FunctionCall<I, O> {


    /**
     * Use the converter to convert the string to the input type, then call the function,
     * and then use the converter to convert the output to a string.
     *
     * @param req The input string for the function call.
     * @return The output string from the function call.
     *
     */
    override fun call(req: String): String =
        inputConverter(req).let(::invoke).let(outputConverter)


    /**
     * Invokes the function call with the given input and returns the output.
     *
     * @param req The input object for the function call.
     * @return The output object from the function call.
     */
    override operator fun invoke(req: I): O = doCall(req)

}

/**
 * A typealias for a function that takes an input and returns an output.
 */
typealias Func1<I, O> = (I) -> O
