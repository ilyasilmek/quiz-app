package com.example.domain.game

/** Pure scoring rules. Production match execution must call the same rules server-side. */
object ScoreCalculator {
    const val BASE_CORRECT = 100
    const val MAX_TIME_BONUS = 180
    const val TIME_BONUS_PER_SECOND = 12

    fun correctAnswerScore(timeRemainingSeconds: Int): Int {
        val bonus = (timeRemainingSeconds.coerceIn(0, 15) * TIME_BONUS_PER_SECOND)
        return BASE_CORRECT + bonus.coerceAtMost(MAX_TIME_BONUS)
    }
}
