package io.kamo.ktor.client.ai.samples

import android.os.Build

object AndroidPlatform : Platform {
    override val name: String = "Android ${Build.VERSION.SDK_INT}"
    override var context: Any? = null
}

actual fun getPlatform(): Platform = AndroidPlatform