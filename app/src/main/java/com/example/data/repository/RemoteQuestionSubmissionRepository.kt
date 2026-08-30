package com.example.data.repository

import com.example.data.remote.PublicQuizApi
import com.example.data.remote.dto.QuestionSubmissionRequest
import com.example.data.remote.dto.QuestionSubmissionResponse

class RemoteQuestionSubmissionRepository(
    private val api: PublicQuizApi,
    private val bearerToken: String
) : QuestionSubmissionRepository {
    override suspend fun submit(request: QuestionSubmissionRequest): Result<QuestionSubmissionResponse> {
        if (bearerToken.isBlank()) {
            return Result.failure(IllegalStateException("Oturum bulunamadı. Lütfen tekrar giriş yapın."))
        }
        return runCatching {
            require(request.options.size == 4) { "Tam olarak 4 şık gönderilmelidir." }
            require(request.correct_index in request.options.indices) { "Doğru cevap geçersiz." }
            require(request.options.map { it.trim().lowercase() }.distinct().size == 4) {
                "Şıklar birbirinden farklı olmalıdır."
            }
            api.submitQuestion(
                bearerToken = "Bearer $bearerToken",
                request = request.copy(
                    category_id = request.category_id.trim(),
                    question_text = request.question_text.trim(),
                    options = request.options.map(String::trim),
                    explanation = request.explanation?.trim()?.ifBlank { null },
                    source_url = request.source_url?.trim()?.ifBlank { null }
                )
            )
        }
    }
}
