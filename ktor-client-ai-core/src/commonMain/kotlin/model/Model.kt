package model;

import kotlinx.coroutines.flow.Flow

interface Model<TReq , TRes> {
	/**
	 * Executes a method call to the AI model.
	 * @param request the request object to be sent to the AI model
	 * @return the response from the AI model
	 */
	suspend fun call(request: TReq): TRes
}


interface StreamingModel<TReq,TResChunk> {
	/**
	 * Executes a method call to the AI model.
	 * @param request the request object to be sent to the AI model
	 * @return the streaming response from the AI model
	 */
	fun stream(request: TReq): Flow<TResChunk>
}