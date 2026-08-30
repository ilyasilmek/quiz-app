package com.example.game

/** Server-compatible scoring formula shared as a reference implementation.
 * Final authoritative score is always calculated by the backend.
 */
object MatchScoreCalculator {
    const val QUESTION_TIME_MS = 15_000
    const val BASE_SCORE = 100
    const val TIME_BONUS_PER_SECOND = 12

    fun score(isCorrect: Boolean, elapsedMs: Long): Int {
        if (!isCorrect) return 0
        val remainingMs = (QUESTION_TIME_MS - elapsedMs.coerceIn(0, QUESTION_TIME_MS)).toInt()
        return BASE_SCORE + (remainingMs / 1000) * TIME_BONUS_PER_SECOND
    }
}
