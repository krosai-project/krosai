package io.kamo.ktor.client.ai.samples.function

import io.kamo.ktor.client.ai.core.chat.function.buildFunctionCall
import io.kamo.ktor.client.ai.core.util.SerialDescription
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

val OpenBrowser = buildFunctionCall {
    name = "date_time"
    description = "Open the browser to jump to the URL corresponding to the name of the site"
    withCall { req: Request ->
        openBrowser("https://www.baidu.com/")
        "Successful opening of the ${req.siteName} web site"
    }
}

@SerialDescription("Open Browser API Request")
@Serializable
data class Request(
    @SerialName("siteName") @SerialDescription("The name of the website")
    val siteName: String
)

expect fun openBrowser(url: String)