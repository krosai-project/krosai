package io.github.krosai.client.ai.samples

class WasmPlatform: Platform {
    override val name: String = "Web with Kotlin/Wasm"
    override var context: Any? = null
}

actual fun getPlatform(): Platform = WasmPlatform()