package io.github.krosai.client.ai.samples

import androidx.compose.ui.Alignment
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState

fun main(): Unit = application {
    val windowState = rememberWindowState(
        position = WindowPosition.Aligned(Alignment.Center)
    )
    Window(
        state = windowState,
        onCloseRequest = ::exitApplication,
        title = "ktor-client-ai-samples",
    ) {
        App()
    }
}

