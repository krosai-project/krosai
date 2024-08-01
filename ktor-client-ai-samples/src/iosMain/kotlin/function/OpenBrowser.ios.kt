package io.kamo.ktor.client.ai.samples.function

import platform.Foundation.NSURL
import platform.UIKit.UIApplication

actual fun openBrowser(url: String) {
    UIApplication.sharedApplication.openURL(NSURL(string = url))
}