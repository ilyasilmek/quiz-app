package com.example.data.remote

import com.example.data.remote.dto.QuestionSubmissionRequest
import com.example.data.remote.dto.QuestionSubmissionResponse
import retrofit2.http.Body
import retrofit2.http.POST

/** Public API only. No admin endpoint is represented in the player client. */
interface PublicGameApi {
    @POST("functions/v1/submit-question")
    suspend fun submitQuestion(@Body request: QuestionSubmissionRequest): QuestionSubmissionResponse
}
