package com.example.data.remote.dto

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class QuestionSubmissionRequest(
    val category_id: String,
    val question: String,
    val options: List<String>,
    val correct_index: Int,
    val explanation: String? = null,
    val source_url: String? = null
)

@JsonClass(generateAdapter = true)
data class QuestionSubmissionResponse(
    val submission_id: String,
    val status: String,
    val duplicate_candidates: Int = 0
)
