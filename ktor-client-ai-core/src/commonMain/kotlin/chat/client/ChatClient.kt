package io.kamo.ktor.client.ai.core.chat.client

import io.kamo.ktor.client.ai.core.chat.function.Func1
import io.kamo.ktor.client.ai.core.chat.function.FunctionCall
import io.kamo.ktor.client.ai.core.chat.function.FunctionCallBuilder
import io.kamo.ktor.client.ai.core.chat.model.ChatResponse
import kotlinx.coroutines.flow.Flow
import kotlin.reflect.KFunction

interface ChatClient {

    suspend fun call(request: ChatClientRequestScopeSpec): ChatResponse

    suspend fun stream(request: ChatClientRequestScopeSpec): Flow<ChatResponse>

}

typealias ChatClientRequestScopeSpec = (ChatClientRequestScope.() -> Unit)?

interface ChatClientRequestScope {

    var model: String

    var userText: Map<String, Any>.() -> String?

    var systemText: Map<String, Any>.() -> String?

    fun user(userScope: PromptUserScope.() -> Unit)

    fun system(systemScope: PromptSystemScope.() -> Unit)

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

interface FunctionCallScope {

    val functionCalls: MutableList<FunctionCall>


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
        withCall(I::class,call)
        build()
    }.also(functionCalls::add)
}
