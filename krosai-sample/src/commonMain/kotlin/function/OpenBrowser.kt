package io.github.krosai.client.ai.samples.function

import io.github.krosai.client.ai.core.chat.function.buildFunctionCall
import io.github.krosai.client.ai.core.util.SerialDescription
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

val OpenBrowser = buildFunctionCall {
    name = "date_time"
    description = "Open the browser to jump to the URL corresponding to the name of the site"
    withCall { req: OpenBrowserRequest ->
        openBrowser(req.siteURL)
        "Successful opening of the ${req.siteURL} web site"
    }
}

@SerialDescription("Open Browser API Request")
@Serializable
data class OpenBrowserRequest(
    @SerialName("siteURL")
    @SerialDescription("URL address of the web site")
    val siteURL: String
)

expect fun openBrowser(url: String)