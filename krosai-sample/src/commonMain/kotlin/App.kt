package org.krosai.sample

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.KoinApplication
import org.krosai.sample.di.AiModule
import org.krosai.sample.ui.ChatBar
import org.krosai.sample.ui.ChatScreen


@Composable
@Preview
fun App() {
    KoinApplication(
        application = {
            modules(AiModule)
        }
    ) {
        MaterialTheme {
            Surface {
                Row {
                    ChatBar()
                    ChatScreen()
                }
            }
        }
    }
}










