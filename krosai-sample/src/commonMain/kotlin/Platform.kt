package org.krosai.sample

interface Platform {
    val name: String
    var context: Any?
}

expect fun getPlatform(): Platform