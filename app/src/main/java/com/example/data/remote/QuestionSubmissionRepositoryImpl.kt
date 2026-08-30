package com.example.data.remote

import java.util.UUID

class QuestionSubmissionRepositoryImpl(
    private val api: QuestionSubmissionApi,
    private val authTokenProvider: AuthTokenProvider,
) {
    suspend fun submit(
        categoryId: String,
        question: String,
        options: List<String>,
        correctIndex: Int,
        explanation: String?,
        sourceUrl: String?,
    ): Result<SubmitQuestionResponse> = runCatching {
        val token = authTokenProvider.accessToken() ?: error("Not authenticated")
        api.submitQuestion(
            SubmitQuestionRequest(
                client_request_id = UUID.randomUUID().toString(),
                category_id = categoryId,
                question = question,
                options = options,
                correct_index = correctIndex,
                explanation = explanation,
                source_url = sourceUrl,
            ),
            "Bearer $token",
        )
    }
}
