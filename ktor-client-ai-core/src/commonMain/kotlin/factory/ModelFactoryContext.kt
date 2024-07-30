package io.kamo.ktor.client.ai.core.factory

class ModelFactoryContext {

    private val factories = mutableMapOf<ModelFactoryBuilder<*, *>, ModelFactory>()

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
