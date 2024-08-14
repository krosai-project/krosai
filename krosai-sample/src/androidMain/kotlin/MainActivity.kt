package org.krosai.sample
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

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
