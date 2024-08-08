package io.github.krosai.core.chat.function

import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull

/**
 * Represents a function call that generates an output of type [O] based on an input of type [I].
 *
 * @param I The input type of the function call.
 * @param O The output type of the function call.
 * @property name The name of the function call.
 * @property description The description of the function call.
 * @property inputConverter The converter function to convert the input string to the input type [I].
 * @property outputConverter The converter function to convert the output type [O] to a string.
 * @property inputSchema The input schema of the function call as a JSON element.
 * @property call The function that performs the generation based on the input.
 *
 * @see AbstractFunctionCall
 * @see Func1
 *
 * @author KAMOsama
 */
class GenerationFunctionCall<in I : Any, out O : Any>(
    name: String,
    description: String,
    inputConverter: Func1<String, I>,
    outputConverter: Func1<O, String> = Any::toString,
    inputSchema: JsonElement = JsonNull,
    call: Func1<I, O>
) : AbstractFunctionCall<I, O>(name, description, inputSchema, inputConverter, outputConverter),
    Func1<I, O> by call