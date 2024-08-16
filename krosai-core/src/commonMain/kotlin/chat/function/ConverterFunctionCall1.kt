package org.krosai.core.chat.function

/**
 * Represents a function call that converts input and output types.
 *
 * @param I The input type of the function call.
 * @param O The output type of the
 *
 * @author KAMOsama
 */
interface ConverterFunctionCall1<I, O> : FunctionCall {


    /**
     * Converts the given input string to the input type of the function call.
     *
     * @param input The input string to be converted.
     * @return The converted input object.
     */
    fun inputConverter(input: String): I

    /**
     * Converts the given output object to a string representation.
     *
     * @param output The output object to be converted.
     * @return The string representation of the output object.
     */
    fun outputConverter(output: O): String

    /**
     * Calls the function call with the given input string and returns the output as a string.
     *
     * @param req The input string to be passed to the function call.
     * @return The output string from the function call.
     */
    override fun call(req: String): String =
        inputConverter(req).let(::invoke).let(::outputConverter)

    /**
     * Invokes the function call with the given input and returns the output.
     *
     * @param input The input object for the function call.
     * @return The output object from the function call.
     */
    operator fun invoke(input: I): O

}