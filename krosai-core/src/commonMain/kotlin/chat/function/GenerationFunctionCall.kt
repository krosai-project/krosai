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
class GenerationFunctionCall<I, O>(
    override val name: String,
    override val description: String,
    override val inputSchema: JsonElement,
    private val doInputConverter: Func1<String, I>,
    private val doOutputConverter: Func1<O, String>,
    private val doCall: Func1<I, O>
) : ConverterFunctionCall1<I, O> {

    /**
     * Converts the input string to the appropriate type.
     *
     * @param input The input string to be converted.
     * @return The converted input value.
     */
    override fun inputConverter(input: String): I = doInputConverter(input)

    /**
     * Converts the output object to a string representation.
     *
     * @param output The output object to be converted.
     * @return The string representation of the output object.
     */
    override fun outputConverter(output: O): String = doOutputConverter(output)

    /**
     * Invokes the function call with the given input and returns the output.
     *
     * @param req The input object for the function call.
     * @return The output object from the function call.
     */
    override operator fun invoke(input: I): O = doCall(input)

}

/**
 * A typealias for a function that takes an input and returns an output.
 */
typealias Func1<I, O> = (I) -> O
