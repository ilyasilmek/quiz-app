package com.example.data.repository

import com.example.data.remote.SubmitQuestionRequest
import com.example.data.remote.SubmitQuestionResponse

interface QuestionSubmissionRepository {
    suspend fun submit(request: SubmitQuestionRequest): Result<SubmitQuestionResponse>
}
