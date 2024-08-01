package io.kamo.ktor.client.ai.samples.function

import kotlinx.browser.window

actual fun openBrowser(url: String) {
    window.open(url)
}