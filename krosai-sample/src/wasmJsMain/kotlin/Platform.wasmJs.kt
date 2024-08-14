package org.krosai.sample

class WasmPlatform: Platform {
    override val name: String = "Web with Kotlin/Wasm"
    override var context: Any? = null
}

actual fun getPlatform(): Platform = WasmPlatform()