package io.github.krosai.samples

object JVMPlatform : Platform {
    override val name: String = "Java ${System.getProperty("java.version")}"
    override var context: Any? = null
}

actual fun getPlatform(): Platform = JVMPlatform