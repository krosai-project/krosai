package org.krosai.sample.function

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.krosai.core.chat.function.buildFunctionCall
import org.krosai.core.util.SerialDescription

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
