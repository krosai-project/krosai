package io.github.krosai.sample.function

import platform.Foundation.NSURL
import platform.UIKit.UIApplication

actual fun openBrowser(url: String) {
    UIApplication.sharedApplication.openURL(NSURL(string = url))
}