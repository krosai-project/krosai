package org.krosai.openai.api.image

enum class OpenAiImageModelEnum(val model: String) {

    DALL_E_2("dall-e-2"),

    DALL_E_3("dall-e-3");

    companion object {
        val DEFAULT = DALL_E_3
    }

}