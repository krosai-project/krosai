package org.krosai.core.model

/**
 * Represents a request object for AI model operations.
 *
 * @param T the type of instructions or input required by the AI model
 *
 * @author KAMOsama
 */
interface ModelRequest<T> {

    /**
     * Retrieves the instructions or input required by the AI model.
     * @return the instructions or input required by the AI model
     */
    val instructions: T // required input

    /**
     * Retrieves the customizable options for AI model interactions.
     * @return the customizable options for AI model interactions
     */
    val options: ModelOptions

}

interface ModelOptions