package io.kamo.ktor.client.ai.samples
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getPlatform().context = this.applicationContext
        setContent {
            App()
        }
    }

    override fun onDestroy() {
        getPlatform().context = null
        super.onDestroy()
    }

}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}