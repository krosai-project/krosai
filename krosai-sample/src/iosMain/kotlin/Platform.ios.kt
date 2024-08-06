package io.github.krosai.client.ai.samples

import platform.UIKit.UIDevice

object IOSPlatform : Platform {
    override val name: String = UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion
    override var context: Any? = null
}

actual fun getPlatform(): Platform = IOSPlatform