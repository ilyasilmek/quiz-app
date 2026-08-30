package com.example.game

import org.junit.Assert.assertEquals
import org.junit.Test

class MatchScoreCalculatorTest {
    @Test fun wrongAnswerScoresZero() {
        assertEquals(0, MatchScoreCalculator.score(false, 1000))
    }

    @Test fun fastCorrectAnswerGetsMaximumBonus() {
        assertEquals(280, MatchScoreCalculator.score(true, 0))
    }

    @Test fun fifteenSecondCorrectAnswerGetsBaseScore() {
        assertEquals(100, MatchScoreCalculator.score(true, 15_000))
    }

    @Test fun elapsedTimeIsClamped() {
        assertEquals(100, MatchScoreCalculator.score(true, 30_000))
        assertEquals(280, MatchScoreCalculator.score(true, -5_000))
    }
}
