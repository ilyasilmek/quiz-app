package com.example.data.remote

import retrofit2.http.Body
import retrofit2.http.POST

/** Public API only. No admin endpoint is represented in the player client. */
interface PublicGameApi {
    @POST("submit-question")
    suspend fun submitQuestion(@Body request: SubmitQuestionRequest): SubmitQuestionResponse
}
