package com.example.data.repository

import com.example.data.remote.AuthTokenProvider
import com.example.data.remote.PublicGameApi
import com.example.data.remote.SubmitQuestionRequest
import com.example.data.remote.SubmitQuestionResponse
import java.util.UUID

class RemoteQuestionSubmissionRepository(
    private val api: PublicGameApi,
    private val authTokenProvider: AuthTokenProvider,
) : QuestionSubmissionRepository {
    override suspend fun submit(request: SubmitQuestionRequest): Result<SubmitQuestionResponse> = runCatching {
        require(request.category_id.isNotBlank()) { "Kategori gerekli." }
        require(request.question.trim().length >= 10) { "Soru çok kısa." }
        require(request.options.size == 4 && request.options.all { it.isNotBlank() }) {
            "Tam olarak 4 dolu şık gönderilmelidir."
        }
        require(request.options.map { it.trim().lowercase() }.distinct().size == 4) {
            "Şıklar birbirinden farklı olmalıdır."
        }
        require(request.correct_index in 0..3) { "Doğru cevap geçersiz." }

        val token = authTokenProvider.accessToken()
            ?.takeIf { it.isNotBlank() }
            ?: error("Oturum bulunamadı. Lütfen tekrar giriş yapın.")

        api.submitQuestion(
            request = request.copy(
                client_request_id = request.client_request_id.ifBlank { UUID.randomUUID().toString() },
                category_id = request.category_id.trim(),
                question = request.question.trim(),
                options = request.options.map(String::trim),
                explanation = request.explanation?.trim()?.ifBlank { null },
                source_url = request.source_url?.trim()?.ifBlank { null },
            ),
        )
    }
}
