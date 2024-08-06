package io.github.krosai.sample.data

import androidx.compose.runtime.MutableState

data class ChatMessage(
    val textState: MutableState<String>,
    val isUser: Boolean
)