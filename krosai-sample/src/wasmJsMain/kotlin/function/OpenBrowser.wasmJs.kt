package io.github.krosai.samples.function

import kotlinx.browser.window

actual fun openBrowser(url: String) {
    window.open(url)
}