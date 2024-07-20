package io.kamo.ktor.client.ai.core.function


abstract class AbstractFunctionCall<in I: Any,out O: Any> (
    override val name: String,
    override val description: String,
    private val inputTypeConverter: Func1<String, I>,
    private val outputTypeConverter: Func1<O, String> = { it.toString() },
): FunctionCall, Func1<I, O> {

    override fun call(req: String): String {
        return outputTypeConverter(this(inputTypeConverter(req)))
    }
}

typealias Func1<I,O> = (I) -> O