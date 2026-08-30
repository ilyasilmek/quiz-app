package com.example.domain

import com.example.domain.content.QuestionValidator
import com.example.domain.game.ScoreCalculator
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ContentAndGameRulesTest {
    @Test
    fun `question validator rejects duplicate options`() {
        val result = QuestionValidator.validate(
            question = "Bu yeterince uzun bir soru metnidir?",
            options = listOf("Ankara", "Ankara", "İzmir", "Bursa"),
            correctIndex = 0
        )
        assertFalse(result.valid)
    }

    @Test
    fun `question validator accepts four distinct options`() {
        val result = QuestionValidator.validate(
            question = "Türkiye'nin başkenti neresidir?",
            options = listOf("Ankara", "İstanbul", "İzmir", "Bursa"),
            correctIndex = 0
        )
        assertTrue(result.valid)
    }

    @Test
    fun `score is deterministic and time based`() {
        assertEquals(100, ScoreCalculator.correctAnswerScore(0))
        assertEquals(280, ScoreCalculator.correctAnswerScore(15))
    }
}
