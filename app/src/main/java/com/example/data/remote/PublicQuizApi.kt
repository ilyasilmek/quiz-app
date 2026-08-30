package com.example.data.remote

import com.squareup.moshi.JsonClass
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

@JsonClass(generateAdapter = true)
data class QuestionSubmissionRequest(
    val category_id: String,
    val question_text: String,
    val options: List<String>,
    val correct_index: Int,
    val explanation: String? = null,
    val source_url: String? = null
)

@JsonClass(generateAdapter = true)
data class QuestionSubmissionResponse(
    val id: String,
    val status: String,
    val duplicate_candidates: Int = 0
)

@JsonClass(generateAdapter = true)
data class ApiErrorResponse(val error: String? = null, val message: String? = null)

interface PublicQuizApi {
    @POST("submit-question")
    suspend fun submitQuestion(
        @Header("Authorization") bearerToken: String,
        @Body request: QuestionSubmissionRequest
    ): QuestionSubmissionResponse
}
