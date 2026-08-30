package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.screens.ActiveQuizScreen
import com.example.ui.screens.AiQuizGeneratorScreen
import com.example.ui.screens.CategoryScreen
import com.example.ui.screens.CreateQuizScreen
import com.example.ui.screens.GameResultScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.LoginScreen
import com.example.ui.screens.MatchRoundsScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.QuizDetailScreen
import com.example.ui.screens.SplashScreen
import com.example.ui.screens.StatisticsScreen
import com.example.ui.screens.QuestionSuggestionScreen
import com.example.ui.theme.BrandBgNavy
import com.example.ui.theme.MyApplicationTheme
import com.example.data.remote.AuthTokenProvider
import com.example.data.remote.QuestionSubmissionSession
import com.example.data.remote.RemoteQuestionSubmissionRepository
import com.example.data.remote.SupabaseApiFactory
import com.example.viewmodel.QuestionSuggestionViewModel
import com.example.viewmodel.Screen
import com.example.viewmodel.TriviaViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = BrandBgNavy
                ) {
                    TriviaApp()
                }
            }
        }
    }
}

@Composable
fun TriviaApp(viewModel: TriviaViewModel = viewModel()) {
    val currentScreen by viewModel.currentScreen.collectAsState()
    val userProfile by viewModel.userProfile.collectAsState()
    val selectedQuiz by viewModel.selectedQuiz.collectAsState()
    val activeMatchState by viewModel.activeMatchState.collectAsState()
    val lastMatchResult by viewModel.lastMatchResult.collectAsState()
    val dailyRewardClaimed by viewModel.dailyRewardClaimed.collectAsState()
    val selectedCategoryTab by viewModel.selectedCategoryTab.collectAsState()

    // Manual Quiz creation states
    val createQuizStep by viewModel.createQuizStep.collectAsState()
    val currentDraftQuestion by viewModel.currentDraftQuestion.collectAsState()
    val draftQuestionList by viewModel.draftQuestionList.collectAsState()

    // AI Quiz states
    val aiTopic by viewModel.aiTopic.collectAsState()
    val aiQuestionCount by viewModel.aiQuestionCount.collectAsState()
    val aiDifficulty by viewModel.aiDifficulty.collectAsState()
    val isGeneratingAiQuiz by viewModel.isGeneratingAiQuiz.collectAsState()
    val suggestionRepository = remember {
        RemoteQuestionSubmissionRepository(
            api = SupabaseApiFactory.questionSubmissionApi(BuildConfig.PUBLIC_API_BASE_URL),
            authTokenProvider = AuthTokenProvider { QuestionSubmissionSession.accessToken() },
        )
    }
    val suggestionViewModel: QuestionSuggestionViewModel = viewModel(
        factory = QuestionSuggestionViewModel.factory(suggestionRepository),
    )
    val suggestionCategory by suggestionViewModel.category.collectAsState()
    val suggestionQuestion by suggestionViewModel.question.collectAsState()
    val suggestionOptions by suggestionViewModel.options.collectAsState()
    val suggestionCorrectIndex by suggestionViewModel.correctIndex.collectAsState()
    val suggestionExplanation by suggestionViewModel.explanation.collectAsState()
    val suggestionSource by suggestionViewModel.source.collectAsState()
    val suggestionSubmitted by suggestionViewModel.submitted.collectAsState()
    val suggestionIsSubmitting by suggestionViewModel.isSubmitting.collectAsState()
    val suggestionError by suggestionViewModel.submissionError.collectAsState()
    var showQuestionSuggestion by remember { mutableStateOf(false) }

    if (showQuestionSuggestion) {
        QuestionSuggestionScreen(
            category = suggestionCategory,
            question = suggestionQuestion,
            options = suggestionOptions,
            correctIndex = suggestionCorrectIndex,
            explanation = suggestionExplanation,
            source = suggestionSource,
            submitted = suggestionSubmitted,
            isSubmitting = suggestionIsSubmitting,
            submissionError = suggestionError,
            onCategoryChange = suggestionViewModel::setCategory,
            onQuestionChange = suggestionViewModel::setQuestion,
            onOptionChange = suggestionViewModel::setOption,
            onCorrectChange = suggestionViewModel::setCorrect,
            onExplanationChange = suggestionViewModel::setExplanation,
            onSourceChange = suggestionViewModel::setSource,
            onSubmit = suggestionViewModel::submit,
            onReset = suggestionViewModel::reset,
            onBack = { showQuestionSuggestion = false }
        )
        return
    }

    Crossfade(targetState = currentScreen, label = "screen_transition") { screen ->
        when (screen) {
            Screen.SPLASH -> {
                SplashScreen(
                    onSplashFinished = { viewModel.navigateTo(Screen.HOME) }
                )
            }

            Screen.LOGIN -> {
                LoginScreen(
                    onLoginSuccess = { viewModel.navigateTo(Screen.HOME) }
                )
            }

            Screen.HOME -> {
                HomeScreen(
                    userProfile = userProfile,
                    dailyRewardClaimed = dailyRewardClaimed,
                    onClaimDailyReward = { viewModel.claimDailyReward() },
                    onStartMatch = { mode -> viewModel.startMatch(mode) },
                    onNavigate = { dest -> viewModel.navigateTo(dest) }
                )
            }

            Screen.CATEGORIES -> {
                CategoryScreen(
                    selectedTab = selectedCategoryTab,
                    onTabSelected = { tab -> viewModel.selectCategoryTab(tab) },
                    onCategoryClick = { cat -> viewModel.selectCategory(cat) },
                    onNavigate = { dest -> viewModel.navigateTo(dest) },
                    onBack = { viewModel.navigateTo(Screen.HOME) }
                )
            }

            Screen.QUIZ_DETAIL -> {
                QuizDetailScreen(
                    quiz = selectedQuiz,
                    onStartGame = { viewModel.startMatch("1v1", selectedQuiz) },
                    onBack = { viewModel.navigateTo(Screen.CATEGORIES) }
                )
            }

            Screen.MATCH_ROUNDS -> {
                MatchRoundsScreen(
                    userProfile = userProfile,
                    matchState = activeMatchState,
                    onProceedToQuiz = { viewModel.navigateTo(Screen.ACTIVE_QUIZ) }
                )
            }

            Screen.ACTIVE_QUIZ -> {
                ActiveQuizScreen(
                    matchState = activeMatchState,
                    onSelectAnswer = { index -> viewModel.selectAnswer(index) },
                    onSkipQuestion = { viewModel.skipQuestion() },
                    onUseFiftyFifty = { viewModel.useFiftyFifty() },
                    onUseAudiencePoll = { viewModel.useAudiencePoll() },
                    onBack = { viewModel.navigateTo(Screen.HOME) }
                )
            }

            Screen.GAME_RESULT -> {
                GameResultScreen(
                    userProfile = userProfile,
                    result = lastMatchResult,
                    onContinue = { viewModel.navigateTo(Screen.HOME) }
                )
            }

            Screen.CREATE_QUIZ -> {
                CreateQuizScreen(
                    step = createQuizStep,
                    currentQuestion = currentDraftQuestion,
                    draftCount = draftQuestionList.size,
                    onStepChange = { step -> viewModel.setCreateQuizStep(step) },
                    onQuestionTextChange = { text -> viewModel.updateDraftQuestionText(text) },
                    onOptionChange = { idx, opt -> viewModel.updateDraftOption(idx, opt) },
                    onCorrectOptionChange = { idx -> viewModel.setDraftCorrectOption(idx) },
                    onAddQuestion = { viewModel.addDraftQuestion() },
                    onPublish = { viewModel.publishManualQuiz() },
                    onSuggestQuestion = { showQuestionSuggestion = true },
                    onSwitchToAi = { viewModel.navigateTo(Screen.AI_GENERATOR) },
                    onBack = { viewModel.navigateTo(Screen.HOME) }
                )
            }

            Screen.AI_GENERATOR -> {
                AiQuizGeneratorScreen(
                    topic = aiTopic,
                    questionCount = aiQuestionCount,
                    difficulty = aiDifficulty,
                    isGenerating = isGeneratingAiQuiz,
                    onTopicChange = { topic -> viewModel.setAiTopic(topic) },
                    onQuestionCountChange = { count -> viewModel.setAiQuestionCount(count) },
                    onDifficultyChange = { diff -> viewModel.setAiDifficulty(diff) },
                    onGenerate = { viewModel.generateAiQuiz() },
                    onSwitchToManual = { viewModel.navigateTo(Screen.CREATE_QUIZ) },
                    onNavigate = { dest -> viewModel.navigateTo(dest) },
                    onBack = { viewModel.navigateTo(Screen.HOME) }
                )
            }

            Screen.STATISTICS -> {
                StatisticsScreen(
                    userProfile = userProfile,
                    onBack = { viewModel.navigateTo(Screen.PROFILE) }
                )
            }

            Screen.PROFILE -> {
                ProfileScreen(
                    userProfile = userProfile,
                    onNavigate = { dest -> viewModel.navigateTo(dest) }
                )
            }
        }
    }
}
