package org.krosai.core.chat.function

/**
 * Options that implement this interface represent function calls that the model can configure
 *
 * @author KAMOsama
 */
interface FunctionCallOptions {

    val functionCalls: MutableList<FunctionCall<*, *>>

    val functionNames: MutableSet<String>

}
