package io.github.krosai.sample

object JVMPlatform : Platform {
    override val name: String = "Java ${System.getProperty("java.version")}"
    override var context: Any? = null
}

actual fun getPlatform(): Platform = JVMPlatform