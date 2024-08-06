package io.github.krosai.client.ai.samples.function

import platform.Foundation.NSURL
import platform.UIKit.UIApplication

actual fun openBrowser(url: String) {
    UIApplication.sharedApplication.openURL(NSURL(string = url))
}