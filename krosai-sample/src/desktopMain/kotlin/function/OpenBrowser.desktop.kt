package org.krosai.sample.function

import java.awt.Desktop
import java.net.URI

actual fun openBrowser(url: String) {
    Desktop.getDesktop().browse(URI(url))
}