package io.kamo.ktor.client.ai.core

import io.kamo.ktor.client.ai.core.model.ChatModel
import io.kamo.ktor.client.ai.core.model.EmbeddingModel
import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.util.*

val AiPluginKey = AttributeKey<AiPlugin<*>>("AiPlugin")

fun <PluginConfigT : Any> createAiPlugin(
    name: String,
    createConfiguration: () -> PluginConfigT,
    body: AiPluginBuilder<PluginConfigT>.() -> Unit
): AiPlugin<PluginConfigT> =
    object : AiPlugin<PluginConfigT> {
        override val key: AttributeKey<AiPluginInstance<PluginConfigT>> = AttributeKey(name)


        override fun prepare(block: PluginConfigT.() -> Unit): AiPluginInstance<PluginConfigT> {
            val config = createConfiguration().apply(block)
            return AiPluginInstance(name, config, body)
        }

        override fun install(plugin: AiPluginInstance<PluginConfigT>, scope: HttpClient) {
            check(scope.attributes.contains(AiPluginKey).not()){"AiPlugin already installed"}
            scope.attributes.put(AiPluginKey, this)
            plugin.install(scope)
        }
    }

interface AiPlugin<PluginConfig : Any> : HttpClientPlugin<PluginConfig, AiPluginInstance<PluginConfig>>

class AiPluginInstance<PluginConfig : Any> internal constructor(
    val name: String,
    private val config: PluginConfig,
    private val body: AiPluginBuilder<PluginConfig>.() -> Unit
) {
    lateinit var chatModel: ChatModel
    lateinit var embeddingModel: EmbeddingModel
    fun install(client: HttpClient) {
        val aiPluginBuilder = AiPluginBuilder(client, config).apply(body)
        aiPluginBuilder.chatModel?.let { this.chatModel = it }
        aiPluginBuilder.embeddingModel?.let { this.embeddingModel = it }
    }

}

class AiPluginBuilder<PluginConfig>(
    val client: HttpClient,
    val config: PluginConfig
) {
    var chatModel: ChatModel? = null
    var embeddingModel: EmbeddingModel? = null
}