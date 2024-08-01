package io.kamo.ktor.client.ai.samples.data

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.mutableStateOf

enum class ChatPath(val path: String, val parNAme: String) {
    CHAT("/chat", "message"),
    SEARCH("/search", "question")
}

val LocaleChatPath = compositionLocalOf { mutableStateOf(ChatPath.CHAT) }