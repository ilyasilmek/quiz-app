package com.example.data.validation

import org.junit.Assert.assertThrows
import org.junit.Assert.assertDoesNotThrow
import org.junit.Test

class QuestionSubmissionValidatorTest {
    private val options = listOf("Ankara", "İstanbul", "İzmir", "Bursa")

    @Test
    fun acceptsValidQuestion() {
        assertDoesNotThrow {
            QuestionSubmissionValidator.validate(
                categoryId = "history",
                question = "Türkiye'nin başkenti neresidir?",
                options = options,
                correctIndex = 0
            )
        }
    }

    @Test
    fun rejectsDuplicateOptions() {
        assertThrows(IllegalArgumentException::class.java) {
            QuestionSubmissionValidator.validate(
                categoryId = "history",
                question = "Türkiye'nin başkenti neresidir?",
                options = listOf("Ankara", "Ankara", "İzmir", "Bursa"),
                correctIndex = 0
            )
        }
    }

    @Test
    fun rejectsInvalidCorrectAnswer() {
        assertThrows(IllegalArgumentException::class.java) {
            QuestionSubmissionValidator.validate(
                categoryId = "history",
                question = "Türkiye'nin başkenti neresidir?",
                options = options,
                correctIndex = 4
            )
        }
    }

    @Test
    fun rejectsShortQuestion() {
        assertThrows(IllegalArgumentException::class.java) {
            QuestionSubmissionValidator.validate(
                categoryId = "history",
                question = "Başkent?",
                options = options,
                correctIndex = 0
            )
        }
    }
}
