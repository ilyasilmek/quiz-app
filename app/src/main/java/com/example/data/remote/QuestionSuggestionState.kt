package com.example.data.remote

data class QuestionSuggestionState(
    val submitting: Boolean = false,
    val submitted: Boolean = false,
    val errorMessage: String? = null,
    val submissionId: String? = null,
    val status: String? = null,
    val duplicateCandidates: Int = 0,
)
