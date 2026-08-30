package com.example.data.repository

import com.example.data.remote.dto.QuestionSubmissionRequest
import com.example.data.remote.dto.QuestionSubmissionResponse

interface QuestionSubmissionRepository {
    suspend fun submit(request: QuestionSubmissionRequest): Result<QuestionSubmissionResponse>
}
