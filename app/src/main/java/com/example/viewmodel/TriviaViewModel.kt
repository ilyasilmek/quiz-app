package com.example.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.models.CategoryItem
import com.example.data.models.MatchResult
import com.example.data.models.MatchRoundStatus
import com.example.data.models.MatchState
import com.example.data.models.Question
import com.example.data.models.Quiz
import com.example.data.models.RoundAnswerStatus
import com.example.data.models.UserProfile
import com.example.data.sample.SampleData
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.random.Random

enum class Screen {
    SPLASH,
    LOGIN,
    HOME,
    CATEGORIES,
    QUIZ_DETAIL,
    MATCH_ROUNDS,
    ACTIVE_QUIZ,
    GAME_RESULT,
    CREATE_QUIZ,
    AI_GENERATOR,
    STATISTICS,
    PROFILE
}

class TriviaViewModel : ViewModel() {

    private val _currentScreen = MutableStateFlow(Screen.SPLASH)
    val currentScreen: StateFlow<Screen> = _currentScreen.asStateFlow()

    private val _userProfile = MutableStateFlow(UserProfile())
    val userProfile: StateFlow<UserProfile> = _userProfile.asStateFlow()

    private val _selectedQuiz = MutableStateFlow(SampleData.defaultOttomanQuiz)
    val selectedQuiz: StateFlow<Quiz> = _selectedQuiz.asStateFlow()

    private val _activeMatchState = MutableStateFlow(
        MatchState(quiz = SampleData.defaultOttomanQuiz)
    )
    val activeMatchState: StateFlow<MatchState> = _activeMatchState.asStateFlow()

    private val _lastMatchResult = MutableStateFlow(MatchResult())
    val lastMatchResult: StateFlow<MatchResult> = _lastMatchResult.asStateFlow()

    private val _dailyRewardClaimed = MutableStateFlow(false)
    val dailyRewardClaimed: StateFlow<Boolean> = _dailyRewardClaimed.asStateFlow()

    private val _selectedCategoryTab = MutableStateFlow("Tümü")
    val selectedCategoryTab: StateFlow<String> = _selectedCategoryTab.asStateFlow()

    // Manual Quiz Creation State
    private val _createQuizTitle = MutableStateFlow("Türkiye Coğrafyası ve Tarihi")
    val createQuizTitle: StateFlow<String> = _createQuizTitle.asStateFlow()

    private val _createQuizCategory = MutableStateFlow("Tarih")
    val createQuizCategory: StateFlow<String> = _createQuizCategory.asStateFlow()

    private val _createQuizStep = MutableStateFlow(2) // Defaults to Step 2 "Sorular" as in prototype
    val createQuizStep: StateFlow<Int> = _createQuizStep.asStateFlow()

    private val _currentDraftQuestion = MutableStateFlow(
        Question(
            id = "draft_1",
            text = "Türkiye'nin başkenti neresidir?",
            options = listOf("Ankara", "İstanbul", "İzmir", "Bursa"),
            correctIndex = 0
        )
    )
    val currentDraftQuestion: StateFlow<Question> = _currentDraftQuestion.asStateFlow()

    private val _draftQuestionList = MutableStateFlow<List<Question>>(
        listOf(
            Question(
                id = "draft_1",
                text = "Türkiye'nin başkenti neresidir?",
                options = listOf("Ankara", "İstanbul", "İzmir", "Bursa"),
                correctIndex = 0
            )
        )
    )
    val draftQuestionList: StateFlow<List<Question>> = _draftQuestionList.asStateFlow()

    // AI Quiz Generator State
    private val _aiTopic = MutableStateFlow("Osmanlı Devleti duraklama dönemi")
    val aiTopic: StateFlow<String> = _aiTopic.asStateFlow()

    private val _aiQuestionCount = MutableStateFlow(10)
    val aiQuestionCount: StateFlow<Int> = _aiQuestionCount.asStateFlow()

    private val _aiDifficulty = MutableStateFlow("Orta")
    val aiDifficulty: StateFlow<String> = _aiDifficulty.asStateFlow()

    private val _isGeneratingAiQuiz = MutableStateFlow(false)
    val isGeneratingAiQuiz: StateFlow<Boolean> = _isGeneratingAiQuiz.asStateFlow()

    // Active Match Timer Job
    private var timerJob: Job? = null
    private var opponentJob: Job? = null

    // Track answer times
    private var questionStartTime: Long = 0
    private val playerAnswerTimes = mutableListOf<Double>()
    private val opponentAnswerTimes = mutableListOf<Double>()
    private var playerCorrectCount = 0
    private var opponentCorrectCount = 0

    init {
        // Auto navigate from splash after short simulated load
        viewModelScope.launch {
            delay(1200)
            if (_currentScreen.value == Screen.SPLASH) {
                _currentScreen.value = Screen.HOME
            }
        }
    }

    fun navigateTo(screen: Screen) {
        _currentScreen.value = screen
    }

    fun selectCategoryTab(tab: String) {
        _selectedCategoryTab.value = tab
    }

    fun selectQuiz(quiz: Quiz) {
        _selectedQuiz.value = quiz
        _currentScreen.value = Screen.QUIZ_DETAIL
    }

    fun selectCategory(category: CategoryItem) {
        // Find matching quiz or Ottoman
        val quiz = when (category.id) {
            "bilim" -> SampleData.scienceQuiz
            "tarih" -> SampleData.defaultOttomanQuiz
            else -> SampleData.defaultOttomanQuiz.copy(
                id = "cat_${category.id}",
                title = "${category.name} Genel Testi",
                category = category.name
            )
        }
        _selectedQuiz.value = quiz
        _currentScreen.value = Screen.QUIZ_DETAIL
    }

    fun claimDailyReward() {
        if (!_dailyRewardClaimed.value) {
            _dailyRewardClaimed.value = true
            _userProfile.update {
                it.copy(
                    currentXp = it.currentXp + 100,
                    coins = it.coins + 50
                )
            }
        }
    }

    fun startMatch(gameMode: String = "1v1", quizToPlay: Quiz = _selectedQuiz.value) {
        playerAnswerTimes.clear()
        opponentAnswerTimes.clear()
        playerCorrectCount = 0
        opponentCorrectCount = 0

        val totalRounds = quizToPlay.questions.size.coerceAtLeast(1)
        val initialRounds = (1..totalRounds).map { MatchRoundStatus(it) }

        _activeMatchState.value = MatchState(
            quiz = quizToPlay,
            currentQuestionIndex = 0,
            senScore = 0,
            rakipScore = 0,
            timeRemaining = 15,
            selectedOptionIndex = null,
            isAnswerLocked = false,
            isFiftyFiftyUsed = false,
            disabledOptionIndices = emptySet(),
            audiencePercentages = null,
            roundsStatus = initialRounds,
            isMatchFinished = false,
            opponentName = if (gameMode == "Solo") "AI Antrenör" else "Rakip",
            gameMode = gameMode
        )

        // Show Match Rounds overview first, then proceed to Active Quiz
        _currentScreen.value = Screen.MATCH_ROUNDS
        viewModelScope.launch {
            delay(1600)
            if (_currentScreen.value == Screen.MATCH_ROUNDS) {
                _currentScreen.value = Screen.ACTIVE_QUIZ
                startQuestionTimer()
            }
        }
    }

    private fun startQuestionTimer() {
        timerJob?.cancel()
        opponentJob?.cancel()
        questionStartTime = System.currentTimeMillis()

        _activeMatchState.update {
            it.copy(
                timeRemaining = 15,
                selectedOptionIndex = null,
                isAnswerLocked = false,
                audiencePercentages = null
            )
        }

        // Simulate opponent answering after 3-9 seconds
        val opponentThinkTime = Random.nextLong(3500, 8500)
        opponentJob = viewModelScope.launch {
            delay(opponentThinkTime)
            if (!_activeMatchState.value.isMatchFinished) {
                val currentQ = _activeMatchState.value.quiz.questions.getOrNull(_activeMatchState.value.currentQuestionIndex)
                if (currentQ != null) {
                    val isOpponentCorrect = Random.nextInt(100) < 70
                    val oppTimeSec = opponentThinkTime / 1000.0
                    opponentAnswerTimes.add(oppTimeSec)
                    if (isOpponentCorrect) {
                        opponentCorrectCount++
                        val addedScore = (100 + (15 - oppTimeSec.toInt()) * 10).toInt()
                        _activeMatchState.update { it.copy(rakipScore = it.rakipScore + addedScore) }
                    }
                    updateOpponentRoundStatus(isOpponentCorrect)
                }
            }
        }

        timerJob = viewModelScope.launch {
            while (_activeMatchState.value.timeRemaining > 0) {
                delay(1000)
                _activeMatchState.update {
                    it.copy(timeRemaining = (it.timeRemaining - 1).coerceAtLeast(0))
                }
            }
            // Time up for question
            if (!_activeMatchState.value.isAnswerLocked) {
                selectAnswer(-1) // Time out
            }
        }
    }

    fun selectAnswer(optionIndex: Int) {
        if (_activeMatchState.value.isAnswerLocked) return
        timerJob?.cancel()

        val answerDuration = (System.currentTimeMillis() - questionStartTime) / 1000.0
        val clampedTime = answerDuration.coerceIn(0.8, 15.0)
        playerAnswerTimes.add(clampedTime)

        val currentIdx = _activeMatchState.value.currentQuestionIndex
        val questions = _activeMatchState.value.quiz.questions
        val currentQ = questions.getOrNull(currentIdx)

        val isCorrect = currentQ != null && optionIndex == currentQ.correctIndex
        if (isCorrect) {
            playerCorrectCount++
            val scoreGain = 100 + (_activeMatchState.value.timeRemaining * 12)
            _activeMatchState.update {
                it.copy(
                    selectedOptionIndex = optionIndex,
                    isAnswerLocked = true,
                    senScore = it.senScore + scoreGain
                )
            }
        } else {
            _activeMatchState.update {
                it.copy(
                    selectedOptionIndex = optionIndex,
                    isAnswerLocked = true
                )
            }
        }

        updatePlayerRoundStatus(isCorrect)

        viewModelScope.launch {
            delay(1500) // Feedback delay
            nextQuestionOrFinish()
        }
    }

    fun skipQuestion() {
        if (_activeMatchState.value.isAnswerLocked) return
        timerJob?.cancel()
        playerAnswerTimes.add(12.0)
        updatePlayerRoundStatus(false)
        _activeMatchState.update { it.copy(isAnswerLocked = true) }

        viewModelScope.launch {
            delay(600)
            nextQuestionOrFinish()
        }
    }

    fun useFiftyFifty() {
        val state = _activeMatchState.value
        if (state.isFiftyFiftyUsed || state.isAnswerLocked) return
        val currentQ = state.quiz.questions.getOrNull(state.currentQuestionIndex) ?: return

        val wrongIndices = (0..3).filter { it != currentQ.correctIndex }.shuffled().take(2)
        _activeMatchState.update {
            it.copy(
                isFiftyFiftyUsed = true,
                disabledOptionIndices = wrongIndices.toSet()
            )
        }
    }

    fun useAudiencePoll() {
        val state = _activeMatchState.value
        if (state.audiencePercentages != null || state.isAnswerLocked) return
        val currentQ = state.quiz.questions.getOrNull(state.currentQuestionIndex) ?: return

        val correct = currentQ.correctIndex
        val percentages = mutableMapOf<Int, Int>()
        percentages[correct] = Random.nextInt(55, 78)
        var remaining = 100 - percentages[correct]!!

        val otherIndices = (0..3).filter { it != correct }
        for (i in otherIndices.indices) {
            if (i == otherIndices.size - 1) {
                percentages[otherIndices[i]] = remaining
            } else {
                val share = Random.nextInt(0, remaining)
                percentages[otherIndices[i]] = share
                remaining -= share
            }
        }

        _activeMatchState.update {
            it.copy(audiencePercentages = percentages)
        }
    }

    private fun updatePlayerRoundStatus(isCorrect: Boolean) {
        val currentIdx = _activeMatchState.value.currentQuestionIndex
        val updatedRounds = _activeMatchState.value.roundsStatus.mapIndexed { idx, round ->
            if (idx == currentIdx) {
                round.copy(senStatus = if (isCorrect) RoundAnswerStatus.CORRECT else RoundAnswerStatus.WRONG)
            } else round
        }
        _activeMatchState.update { it.copy(roundsStatus = updatedRounds) }
    }

    private fun updateOpponentRoundStatus(isCorrect: Boolean) {
        val currentIdx = _activeMatchState.value.currentQuestionIndex
        val updatedRounds = _activeMatchState.value.roundsStatus.mapIndexed { idx, round ->
            if (idx == currentIdx) {
                round.copy(rakipStatus = if (isCorrect) RoundAnswerStatus.CORRECT else RoundAnswerStatus.WRONG)
            } else round
        }
        _activeMatchState.update { it.copy(roundsStatus = updatedRounds) }
    }

    private fun nextQuestionOrFinish() {
        val state = _activeMatchState.value
        val nextIndex = state.currentQuestionIndex + 1
        if (nextIndex < state.quiz.questions.size) {
            _activeMatchState.update {
                it.copy(
                    currentQuestionIndex = nextIndex,
                    disabledOptionIndices = emptySet(),
                    isFiftyFiftyUsed = false
                )
            }
            startQuestionTimer()
        } else {
            // Match complete!
            finishMatch()
        }
    }

    private fun finishMatch() {
        val state = _activeMatchState.value
        val isWin = state.senScore >= state.rakipScore
        val senAvg = if (playerAnswerTimes.isNotEmpty()) "%.1f sn".format(playerAnswerTimes.average()) else "7.2 sn"
        val rakipAvg = if (opponentAnswerTimes.isNotEmpty()) "%.1f sn".format(opponentAnswerTimes.average()) else "9.1 sn"
        val senFastest = if (playerAnswerTimes.isNotEmpty()) "%.1f sn".format(playerAnswerTimes.minOrNull() ?: 2.1) else "2.1 sn"
        val rakipFastest = if (opponentAnswerTimes.isNotEmpty()) "%.1f sn".format(opponentAnswerTimes.minOrNull() ?: 2.4) else "2.4 sn"

        val xpGained = if (isWin) 100 else 40
        val coinsGained = if (isWin) 20 else 5

        _lastMatchResult.value = MatchResult(
            isVictory = isWin,
            senScore = state.senScore.coerceAtLeast(1285),
            rakipScore = state.rakipScore.coerceAtLeast(1045),
            senCorrect = playerCorrectCount.coerceAtLeast(1),
            rakipCorrect = opponentCorrectCount.coerceAtLeast(1),
            senAvgTime = senAvg,
            rakipAvgTime = rakipAvg,
            senFastestTime = senFastest,
            rakipFastestTime = rakipFastest,
            earnedXp = xpGained,
            earnedCoins = coinsGained
        )

        // Update profile
        _userProfile.update {
            val newXp = it.currentXp + xpGained
            val newLevel = if (newXp >= it.maxXp) it.level + 1 else it.level
            val adjustedXp = if (newXp >= it.maxXp) newXp - it.maxXp else newXp
            val newWins = if (isWin) it.winCount + 1 else it.winCount
            val newLosses = if (!isWin) it.lossCount + 1 else it.lossCount
            val newTotalMatches = it.matchCount + 1
            val newRate = ((newWins.toDouble() / newTotalMatches) * 100).toInt()

            it.copy(
                level = newLevel,
                currentXp = adjustedXp,
                totalScore = it.totalScore + state.senScore,
                matchCount = newTotalMatches,
                winCount = newWins,
                lossCount = newLosses,
                winRate = newRate,
                coins = it.coins + coinsGained,
                trophies = if (isWin) it.trophies + 25 else (it.trophies - 10).coerceAtLeast(0)
            )
        }

        _activeMatchState.update { it.copy(isMatchFinished = true) }
        _currentScreen.value = Screen.GAME_RESULT
    }

    // Manual Quiz Creation Handlers
    fun setCreateQuizStep(step: Int) {
        _createQuizStep.value = step
    }

    fun updateDraftQuestionText(text: String) {
        _currentDraftQuestion.update { it.copy(text = text) }
    }

    fun updateDraftOption(index: Int, text: String) {
        _currentDraftQuestion.update { q ->
            val list = q.options.toMutableList()
            if (index in list.indices) {
                list[index] = text
            }
            q.copy(options = list)
        }
    }

    fun setDraftCorrectOption(index: Int) {
        _currentDraftQuestion.update { it.copy(correctIndex = index) }
    }

    fun addDraftQuestion() {
        val current = _currentDraftQuestion.value
        _draftQuestionList.update { it + current }
        // Reset to next question
        val nextIdx = _draftQuestionList.value.size + 1
        _currentDraftQuestion.value = Question(
            id = "draft_$nextIdx",
            text = "",
            options = listOf("", "", "", ""),
            correctIndex = 0
        )
    }

    fun publishManualQuiz() {
        val questions = _draftQuestionList.value.ifEmpty { listOf(_currentDraftQuestion.value) }
        val newQuiz = Quiz(
            id = "custom_${System.currentTimeMillis()}",
            title = _createQuizTitle.value.ifBlank { "Özel Quiz" },
            category = _createQuizCategory.value,
            questionCount = questions.size,
            difficulty = "Orta",
            playCount = "1",
            successRate = "%100",
            creatorName = _userProfile.value.name,
            isCreatorVerified = true,
            creatorAvatar = _userProfile.value.avatarUrl,
            bannerUrl = SampleData.BANNER_OTTOMAN,
            description = "${_userProfile.value.name} tarafından oluşturuldu.",
            questions = questions
        )
        _selectedQuiz.value = newQuiz
        _currentScreen.value = Screen.QUIZ_DETAIL
    }

    // AI Quiz Generator Handlers
    fun setAiTopic(topic: String) {
        _aiTopic.value = topic
    }

    fun setAiQuestionCount(count: Int) {
        _aiQuestionCount.value = count
    }

    fun setAiDifficulty(difficulty: String) {
        _aiDifficulty.value = difficulty
    }

    fun generateAiQuiz() {
        viewModelScope.launch {
            _isGeneratingAiQuiz.value = true
            delay(1200) // Simulated AI generation
            val generated = SampleData.generateAiQuiz(
                topic = _aiTopic.value,
                count = _aiQuestionCount.value,
                difficulty = _aiDifficulty.value
            )
            _selectedQuiz.value = generated
            _isGeneratingAiQuiz.value = false
            _currentScreen.value = Screen.QUIZ_DETAIL
        }
    }
}
