package io.github.krosai.samples.function

import io.github.krosai.core.chat.function.buildFunctionCall
import io.github.krosai.core.util.SerialDescription
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

val GetURL = buildFunctionCall {
    name = "getURL"
    description = "Get URL based on site name"
    withCall { req: GetURLRequest ->
        "${req.siteName}'s webpage URL address is: https://www.baidu.com"
    }
}

@SerialDescription("Get URL API Request")
@Serializable
data class GetURLRequest(
    @SerialName("siteName")
    @SerialDescription("The name of the website")
    val siteName: String
)
