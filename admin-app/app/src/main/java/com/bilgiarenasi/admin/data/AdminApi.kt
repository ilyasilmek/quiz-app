package com.bilgiarenasi.admin.data

import com.squareup.moshi.JsonClass
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

@JsonClass(generateAdapter = true)
data class AdminQueueItem(
    val id: String,
    val category_id: String,
    val question_text: String,
    val options: List<String>,
    val correct_index: Int,
    val explanation: String?,
    val source_url: String?,
    val status: String,
    val author_id: String,
    val created_at: String,
    val duplicate_candidates: Int = 0
)

@JsonClass(generateAdapter = true)
data class AdminQueueResponse(val items: List<AdminQueueItem>)

@JsonClass(generateAdapter = true)
data class AdminReviewRequest(
    val submission_id: String,
    val action: String,
    val rejection_reason: String? = null
)

interface AdminApi {
    @GET("admin-question-queue")
    suspend fun queue(
        @Query("status") status: String = "pending_review",
        @Header("Authorization") authorization: String,
    ): AdminQueueResponse

    @POST("admin-question-review")
    suspend fun review(
        @Body request: AdminReviewRequest,
        @Header("Authorization") authorization: String,
    ): Map<String, Any?>
}
