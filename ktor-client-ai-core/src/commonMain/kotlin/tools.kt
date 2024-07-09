package io.kamo.ktor.client.ai.core

val SSE_PREDICATE :(String)-> Boolean = { it.isNotEmpty() && it != "[DONE]" }
