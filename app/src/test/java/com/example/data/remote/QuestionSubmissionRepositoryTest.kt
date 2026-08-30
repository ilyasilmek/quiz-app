package com.example.data.remote

import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class QuestionSubmissionRepositoryTest {
    private val api = object : QuestionSubmissionApi {
        override suspend fun submitQuestion(request: SubmitQuestionRequest): SubmitQuestionResponse {
            return SubmitQuestionResponse("test-id", "pending_review", 0)
        }
    }

    @Test
    fun rejectsQuestionWithFewerThanFourOptions() = runTest {
        val result = RemoteQuestionSubmissionRepository(api).submit(
            categoryId = "history",
            question = "Bu yeterince uzun bir soru mu?",
            options = listOf("A", "B", "C"),
            correctIndex = 0,
            explanation = null,
            sourceUrl = null
        )

        assertTrue(result.isFailure)
    }

    @Test
    fun rejectsDuplicateOptionsAfterTrim() = runTest {
        val result = RemoteQuestionSubmissionRepository(api).submit(
            categoryId = "history",
            question = "Bu da yeterince uzun bir soru mu?",
            options = listOf("Ankara", " Ankara ", "İzmir", "Bursa"),
            correctIndex = 0,
            explanation = null,
            sourceUrl = null
        )

        assertTrue(result.isFailure)
    }

    @Test
    fun trimsAndSubmitsValidPayload() = runTest {
        val result = RemoteQuestionSubmissionRepository(api).submit(
            categoryId = "history",
            question = "  Osmanlı Devleti'nin ilk başkenti neresidir?  ",
            options = listOf(" Bursa ", "Edirne", "İstanbul", "Söğüt"),
            correctIndex = 0,
            explanation = " İlk başkent Bursa'dır. ",
            sourceUrl = " https://example.com/source "
        )

        assertTrue(result.isSuccess)
        assertEquals("pending_review", result.getOrThrow().status)
    }
}
