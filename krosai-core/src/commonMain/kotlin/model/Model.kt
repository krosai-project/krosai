package org.krosai.core.model

import kotlinx.coroutines.flow.Flow

interface Model<TReq : ModelRequest<*>, TRes : ModelResponse<*>> {
    /**
     * Executes a method call to the AI model.
     * @param request the request object to be sent to the AI model
     * @return the response from the AI model
     */
    suspend fun call(request: TReq): TRes

}

interface StreamingModel<TReq : ModelRequest<*>, TResChunk : ModelResponse<*>> {

    /**
     * Executes a method call to the AI model.
     * @param request the request object to be sent to the AI model
     * @return the streaming response from the AI model
     */
	suspend fun stream(request: TReq): Flow<TResChunk>

}

