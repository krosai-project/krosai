package io.github.krosai.client.ai.core.chat.client

import io.github.krosai.client.ai.core.chat.enhancer.Enhancer
import io.github.krosai.client.ai.core.chat.function.Func1
import io.github.krosai.client.ai.core.chat.function.FunctionCall
import io.github.krosai.client.ai.core.chat.function.FunctionCallBuilder
import io.github.krosai.client.ai.core.chat.model.ChatResponse
import kotlinx.coroutines.flow.Flow
import kotlin.reflect.KFunction

/**
 * Represents a chat client that interacts with an AI model to send and receive chat messages.
 */
interface ChatClient {

    suspend fun call(requestScopeSpec: ChatClientRequestDefinition): ChatResponse

    suspend fun stream(requestScopeSpec: ChatClientRequestDefinition): Flow<ChatResponse>

}

typealias ChatClientRequestDefinition = (ChatClientRequestScope.() -> Unit)?

interface ChatClientRequestScope {

    var userText: Map<String, Any>.() -> String?

    var systemText: Map<String, Any>.() -> String?

    fun user(userScope: PromptUserScope.() -> Unit)

    fun system(systemScope: PromptSystemScope.() -> Unit)

    fun enhancers(enhancersScope: EnhancersScope.() -> Unit)

    fun functions(functionCallScope: FunctionCallScope.() -> Unit)

}

interface PromptUserScope {

    var text: Map<String, Any>.() -> String?

    infix fun String.to(value: Any)

}

interface PromptSystemScope {

    var text: Map<String, Any>.() -> String?

    infix fun String.to(value: Any)

}

interface EnhancersScope {

    infix fun String.to(value: Any)

    operator fun Enhancer.unaryPlus()

    operator fun List<Enhancer>.unaryPlus()

}

interface FunctionCallScope {

    operator fun FunctionCall.unaryPlus()

    operator fun List<FunctionCall>.unaryPlus()

    operator fun String.unaryPlus()

    operator fun KFunction<*>.unaryPlus()

    fun function(vararg functions: KFunction<*>) =
        functions.map { it.name }.toTypedArray().let { function(*it) }

    fun function(vararg functionNames: String)

}

inline fun <reified I : Any, O : Any> FunctionCallScope.function(
    name: String,
    description: String,
    noinline call: Func1<I, O>
){
    with(FunctionCallBuilder(name, description)) {
        withCall<I>(call)
        build()
    }.also { +it }
}
