package io.kamo.ktor.client.ai.samples.function

import android.content.Context
import android.content.Intent
import android.net.Uri
import io.kamo.ktor.client.ai.samples.getPlatform

actual fun openBrowser(url: String) {
    getPlatform().context?.let {
        when (it) {
            is Context -> {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                it.startActivity(intent)
            }
        }
    }
}