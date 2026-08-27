package com.example.data.models

data class Question(
    val id: String,
    val text: String,
    val options: List<String>,
    val correctIndex: Int,
    val category: String = "Genel",
    val explanation: String = ""
)

data class Quiz(
    val id: String,
    val title: String,
    val category: String,
    val questionCount: Int,
    val difficulty: String, // "Kolay", "Orta", "Zor"
    val playCount: String,
    val successRate: String,
    val creatorName: String,
    val isCreatorVerified: Boolean = true,
    val creatorAvatar: String = "",
    val bannerUrl: String = "",
    val description: String = "",
    val questions: List<Question> = emptyList()
)

data class CategoryItem(
    val id: String,
    val name: String,
    val quizCount: String,
    val icon: String, // icon identifier
    val tintHex: Long,
    val hasPremiumBadge: Boolean = false,
    val tags: List<String> = listOf("Tümü")
)

data class UserProfile(
    val name: String = "İlyas",
    val level: Int = 12,
    val currentXp: Int = 2450,
    val maxXp: Int = 3500,
    val totalScore: Int = 1245,
    val matchCount: Int = 128,
    val winCount: Int = 91,
    val lossCount: Int = 37,
    val winRate: Int = 71,
    val avatarUrl: String = "https://lh3.googleusercontent.com/aida-public/AB6AXuC90-HsQrj5IQC5-G2jAZ8pS0lnOYFmOecOEe3wSDfDZ6JCIDgT6K8oPpaZrQYIdBe5WTY8f75KIfiJG45cfYFMmOur4erP1HOZPtWlwuxmwZcQsx1SK-gQWkXPpqA5UlPQQOFj_RP--4rDyHpKXZkW6jCCuNnrFc16fh_b_bOnLxD9eoDdTxLcVUXVwEwPFocv1O5oZsaaEeInAZRjHVk6T86Nh2X9N_y06edQ-ELUjdf_HcBPycof",
    val trophies: Int = 1340,
    val coins: Int = 340
)

enum class RoundAnswerStatus {
    NONE,
    CORRECT,
    WRONG,
    THINKING
}

data class MatchRoundStatus(
    val roundNumber: Int,
    val senStatus: RoundAnswerStatus = RoundAnswerStatus.NONE,
    val rakipStatus: RoundAnswerStatus = RoundAnswerStatus.NONE
)

data class MatchState(
    val quiz: Quiz,
    val currentQuestionIndex: Int = 0,
    val senScore: Int = 0,
    val rakipScore: Int = 0,
    val timeRemaining: Int = 15,
    val selectedOptionIndex: Int? = null,
    val isAnswerLocked: Boolean = false,
    val isFiftyFiftyUsed: Boolean = false,
    val disabledOptionIndices: Set<Int> = emptySet(),
    val audiencePercentages: Map<Int, Int>? = null,
    val roundsStatus: List<MatchRoundStatus> = (1..10).map { MatchRoundStatus(it) },
    val isMatchFinished: Boolean = false,
    val opponentName: String = "Rakip",
    val opponentTrophies: Int = 1220,
    val opponentAvatar: String = "https://lh3.googleusercontent.com/aida-public/AB6AXuCoQO6q4CKo_DiiPchiIUFlkdp4EWmdneBB8QCYsMfWkMqncUqb3ouYBZZ_8_WQydx78IGX7aKriBHdoS_mABZv3kRijQt5vO_r7U_6igXZyhhb8QISfGKJVw24dsfGEDWoRj-YLC9dDdasl48PkwOWekmq_HCARJwL2PTPeW8aPvDrww3bE3GblMgzsRPqjglWPQvUU1eqlgUgOUENpWhxFGKK2tdNtWcXVwTUlv6GD-ZwTTvuiwUB",
    val gameMode: String = "1v1" // "1v1", "Solo", "Friend"
)

data class MatchResult(
    val isVictory: Boolean = true,
    val senScore: Int = 1285,
    val rakipScore: Int = 1045,
    val senCorrect: Int = 8,
    val rakipCorrect: Int = 6,
    val senAvgTime: String = "7.2 sn",
    val rakipAvgTime: String = "9.1 sn",
    val senFastestTime: String = "2.1 sn",
    val rakipFastestTime: String = "2.4 sn",
    val earnedXp: Int = 100,
    val earnedCoins: Int = 20
)

data class AchievementItem(
    val id: String,
    val title: String,
    val description: String,
    val progress: Int,
    val maxProgress: Int,
    val isUnlocked: Boolean
)

data class FriendItem(
    val id: String,
    val name: String,
    val level: Int,
    val isOnline: Boolean,
    val avatarUrl: String
)
