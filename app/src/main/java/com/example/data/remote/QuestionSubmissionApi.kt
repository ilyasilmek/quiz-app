package com.example.data.remote

import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

data class SubmitQuestionRequest(
    val client_request_id: String,
    val category_id: String,
    val question: String,
    val options: List<String>,
    val correct_index: Int,
    val explanation: String? = null,
    val source_url: String? = null
)

data class SubmitQuestionResponse(
    val submission_id: String,
    val status: String,
    val duplicate_candidates: Int,
    val idempotent_replay: Boolean = false
)

interface QuestionSubmissionApi {
    @POST("submit-question")
    suspend fun submitQuestion(
        @Body request: SubmitQuestionRequest,
        @Header("Authorization") authorization: String,
    ): SubmitQuestionResponse
}
