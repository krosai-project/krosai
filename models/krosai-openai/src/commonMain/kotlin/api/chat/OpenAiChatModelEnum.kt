package org.krosai.openai.api.chat

enum class OpenAiChatModelEnum(val model: String) {

    /**
     * Multimodal flagship model that’s cheaper and faster than GPT-4 Turbo. Currently
     * points to gpt-4o-2024-05-13.
     */
    GPT_4_O("gpt-4o"),

    /**
     * Affordable and intelligent small model for fast, lightweight tasks. GPT-4o mini
     * is cheaper and more capable than GPT-3.5 Turbo. Currently points to
     * gpt-4o-mini-2024-07-18.
     */
    GPT_4_O_MINI("gpt-4o-mini"),

    /**
     * GPT-4 Turbo with Vision The latest GPT-4 Turbo model with vision capabilities.
     * Vision requests can now use JSON mode and function calling. Currently points to
     * gpt-4-turbo-2024-04-09.
     */
    GPT_4_TURBO("gpt-4-turbo"),

    /**
     * GPT-4 Turbo with Vision model. Vision requests can now use JSON mode and
     * function calling.
     */
    GPT_4_TURBO_2204_04_09("gpt-4-turbo-2024-04-09"),

    /**
     * (New) GPT-4 Turbo - latest GPT-4 model intended to reduce cases of “laziness”
     * where the model doesn’t complete a task. Returns a maximum of 4,096 output
     * tokens. Context window: 128k tokens
     */
    GPT_4_0125_PREVIEW("gpt-4-0125-preview"),

    /**
     * Currently points to gpt-4-0125-preview - model featuring improved instruction
     * following, JSON mode, reproducible outputs, parallel function calling, and
     * more. Returns a maximum of 4,096 output tokens Context window: 128k tokens
     */
    GPT_4_TURBO_PREVIEW("gpt-4-turbo-preview"),


    /**
     * Currently points to gpt-4-0613. Snapshot of gpt-4 from June 13th 2023 with
     * improved function calling support. Context window: 8k tokens
     */
    GPT_4("gpt-4"),


    /**
     * Currently points to gpt-3.5-turbo-0125. model with higher accuracy at
     * responding in requested formats and a fix for a bug which caused a text
     * encoding issue for non-English language function calls. Returns a maximum of
     * 4,096 Context window: 16k tokens
     */
    GPT_3_5_TURBO("gpt-3.5-turbo"),

    /**
     * (new) The latest GPT-3.5 Turbo model with higher accuracy at responding in
     * requested formats and a fix for a bug which caused a text encoding issue for
     * non-English language function calls. Returns a maximum of 4,096 Context window:
     * 16k tokens
     */
    GPT_3_5_TURBO_0125("gpt-3.5-turbo-0125"),

    /**
     * GPT-3.5 Turbo model with improved instruction following, JSON mode,
     * reproducible outputs, parallel function calling, and more. Returns a maximum of
     * 4,096 output tokens. Context window: 16k tokens.
     */
    GPT_3_5_TURBO_1106("gpt-3.5-turbo-1106");

    companion object {
        val DEFAULT = GPT_4_O
    }

}