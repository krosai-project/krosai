package io.kamo.ktor.client.ai.core.factory

import io.kamo.ktor.client.ai.core.chat.function.FunctionCall

class ModelFactoryContext {

    private val factories = mutableMapOf<ModelFactoryBuilder<*, *>, ModelFactory>()

    private val functionCallRegister: MutableMap<String, FunctionCall> = mutableMapOf()

    private fun registerFunctionCall(vararg functionCall: FunctionCall) {
        functionCall.forEach {
            functionCallRegister[it.name] = it
        }
    }

    fun getFunctionCallsByName(functionNames: Set<String>): List<FunctionCall> {
        return functionNames.map {
            functionCallRegister[it]
                ?: throw IllegalArgumentException("No function call with name [$it] found in the register")
        }
    }

    fun functions(vararg functionCall: FunctionCall) {
        registerFunctionCall(*functionCall)
    }

    operator fun <F : ModelFactory> get(factoryKey: ModelFactoryBuilder<*, F>): F {
        val modelFactory =
            factories[factoryKey] ?: throw IllegalArgumentException("Model factory not found: $factoryKey")
        @Suppress("UNCHECKED_CAST")
        return modelFactory as F
    }

    internal fun register(builder: ModelFactoryBuilder<*, *>, modelFactory: ModelFactory) {
        factories[builder] = modelFactory
    }

    fun <M : ModelFactoryBuilder<Config, *>, Config> factory(factory: M, block: Config.() -> Unit) {
        factory.config.block()
        factory.install(this)
    }

}

fun buildModelFactoryContext(
    builder: ModelFactoryContext.() -> Unit
): ModelFactoryContext = ModelFactoryContext().apply(builder)
