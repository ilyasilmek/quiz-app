package com.example.data.remote.dto

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class QuestionSubmissionResponse(
    val id: String,
    val status: String,
    val duplicate_candidates: Int = 0
)
