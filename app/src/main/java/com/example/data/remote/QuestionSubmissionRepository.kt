package com.example.data.remote

import java.util.UUID

/** Public player-side contract for question submissions. */
interface QuestionSubmissionRepository {
    suspend fun submit(
        categoryId: String,
        question: String,
        options: List<String>,
        correctIndex: Int,
        explanation: String?,
        sourceUrl: String?,
        clientRequestId: String = UUID.randomUUID().toString(),
    ): Result<SubmitQuestionResponse>
}

class RemoteQuestionSubmissionRepository(
    private val api: QuestionSubmissionApi,
    private val authTokenProvider: AuthTokenProvider,
) : QuestionSubmissionRepository {
    override suspend fun submit(
        categoryId: String,
        question: String,
        options: List<String>,
        correctIndex: Int,
        explanation: String?,
        sourceUrl: String?,
        clientRequestId: String,
    ): Result<SubmitQuestionResponse> = runCatching {
        require(categoryId.isNotBlank()) { "Kategori gerekli" }
        require(question.trim().length >= 10) { "Soru çok kısa" }
        require(options.size == 4 && options.all { it.isNotBlank() }) { "Dört şık gerekli" }
        require(options.map { it.trim().lowercase() }.distinct().size == 4) {
            "Şıklar birbirinden farklı olmalı"
        }
        require(correctIndex in 0..3) { "Geçersiz doğru cevap" }
        require(clientRequestId.isNotBlank()) { "İstek kimliği gerekli" }

        val token = authTokenProvider.accessToken()
            ?.takeIf { it.isNotBlank() }
            ?: error("Oturum bulunamadı. Lütfen tekrar giriş yapın.")

        api.submitQuestion(
            request = SubmitQuestionRequest(
                client_request_id = clientRequestId,
                category_id = categoryId.trim(),
                question = question.trim(),
                options = options.map(String::trim),
                correct_index = correctIndex,
                explanation = explanation?.trim()?.ifBlank { null },
                source_url = sourceUrl?.trim()?.ifBlank { null },
            ),
            authorization = "Bearer $token",
        )
    }
}
