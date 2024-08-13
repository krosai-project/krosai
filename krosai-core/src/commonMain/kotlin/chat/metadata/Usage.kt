package io.github.krosai.core.chat.metadata

/**
 * Represents the usage information of the model.
 *
 * This interface provides information about*/
interface Usage {

    /**
     * Stores the number of prompt tokens.
     *
     * This variable is part of the `Usage` interface and is inherited by classes that implement the `Usage` interface. The total number of tokens, which includes both prompt tokens
     *  and generation tokens, can be calculated using the `totalTokens` property available in the `Usage` interface.
     *
     * @see Usage.promptTokens
     * @see Usage.totalTokens
     */
    val promptTokens: Long

    /**
     * Represents the number of generation tokens.
     *
     * The `generationTokens` variable is used to store the number of tokens produced by the model during generation.
     * Generation tokens represent the tokens generated as output.
     * .
     * @see Usage.totalTokens
     */
    val generationTokens: Long

    /**
     * Represents the total number of tokens.
     *
     * The `totalTokens` variable is part of the `Usage` interface and is inherited by classes that implement the `Usage` interface.
     * It calculates the total number of tokens by adding the number of prompt tokens to the number of generation tokens.
     *
     * @see Usage.promptTokens
     * @see Usage.generationTokens
     */
    val totalTokens: Long
        get() = promptTokens + generationTokens

    /**
     * Represents an empty usage object.
     * promptTokens and generationTokens are set to 0L.
     */
    data object Empty : Usage {
        override val promptTokens: Long = 0L
        override val generationTokens: Long = 0L
    }

}
