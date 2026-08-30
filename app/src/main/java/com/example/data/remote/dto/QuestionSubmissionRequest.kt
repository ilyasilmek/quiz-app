package com.example.data.remote.dto

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class QuestionSubmissionRequest(
    val category_id: String,
    val question_text: String,
    val options: List<String>,
    val correct_index: Int,
    val explanation: String? = null,
    val source_url: String? = null
)
