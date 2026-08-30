package com.example.data.models

enum class QuestionSubmissionStatus {
    PENDING_VALIDATION,
    PENDING_REVIEW,
    APPROVED,
    REJECTED,
    PUBLISHED,
    ARCHIVED
}

data class QuestionSubmission(
    val id: String = "",
    val category: String = "Genel Kültür",
    val question: String = "",
    val options: List<String> = listOf("", "", "", ""),
    val correctIndex: Int = 0,
    val explanation: String = "",
    val source: String = "",
    val status: QuestionSubmissionStatus = QuestionSubmissionStatus.PENDING_VALIDATION
)
