package org.krosai.core.chat.message

import io.ktor.http.*

sealed class Media<T>(
    val contentType: ContentType,
    val content: T
) {

    class Bytes(contentType: ContentType, bytes: ByteArray) : Media<ByteArray>(contentType, bytes)

    class Url(contentType: ContentType, url: io.ktor.http.Url) : Media<io.ktor.http.Url>(contentType, url) {
        constructor(contentType: ContentType, url: String) : this(contentType, Url(url))
    }

}

