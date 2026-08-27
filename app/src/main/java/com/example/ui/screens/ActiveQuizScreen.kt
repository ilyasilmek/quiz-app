package com.example.ui.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.models.MatchState
import com.example.ui.theme.BrandBgNavy
import com.example.ui.theme.BrandError
import com.example.ui.theme.BrandGold
import com.example.ui.theme.BrandPurple
import com.example.ui.theme.BrandPurpleLight
import com.example.ui.theme.BrandSuccess
import com.example.ui.theme.BrandSurfaceNavy
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun ActiveQuizScreen(
    matchState: MatchState,
    onSelectAnswer: (Int) -> Unit,
    onSkipQuestion: () -> Unit,
    onUseFiftyFifty: () -> Unit,
    onUseAudiencePoll: () -> Unit,
    onBack: () -> Unit
) {
    val currentQuestion = matchState.quiz.questions.getOrNull(matchState.currentQuestionIndex)
    val questionNumber = matchState.currentQuestionIndex + 1
    val totalQuestions = matchState.quiz.questions.size
    val progressFraction = (questionNumber.toFloat() / totalQuestions.toFloat()).coerceIn(0f, 1f)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BrandBgNavy)
            .testTag("active_quiz_screen")
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Top Navigation & Progress
            Column {
                // Top Bar
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(BrandSurfaceNavy)
                            .testTag("quiz_back_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Geri",
                            tint = Color.White
                        )
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Puan: ",
                            style = MaterialTheme.typography.labelMedium,
                            color = TextSecondary
                        )
                        Text(
                            text = "${matchState.senScore}",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = BrandGold
                        )
                    }

                    IconButton(
                        onClick = { },
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(BrandSurfaceNavy)
                    ) {
                        Icon(
                            imageVector = Icons.Default.MoreHoriz,
                            contentDescription = "Seçenekler",
                            tint = Color.White
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Progress Bar & Timer Section
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "SORU $questionNumber / $totalQuestions",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = BrandPurpleLight,
                            letterSpacing = 1.sp
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(6.dp)
                                .clip(RoundedCornerShape(3.dp))
                                .background(BrandSurfaceNavy)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(progressFraction)
                                    .height(6.dp)
                                    .clip(RoundedCornerShape(3.dp))
                                    .background(
                                        Brush.horizontalGradient(
                                            listOf(BrandPurple, BrandPurpleLight, BrandGold)
                                        )
                                    )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    // Circular Countdown Timer
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.size(54.dp)
                    ) {
                        CircularProgressIndicator(
                            progress = { matchState.timeRemaining / 15f },
                            modifier = Modifier.fillMaxSize(),
                            color = if (matchState.timeRemaining <= 5) BrandError else BrandGold,
                            trackColor = BrandSurfaceNavy,
                            strokeWidth = 4.dp,
                            strokeCap = StrokeCap.Round
                        )
                        Text(
                            text = "${matchState.timeRemaining}",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = if (matchState.timeRemaining <= 5) BrandError else BrandGold
                        )
                    }
                }
            }

            // Question Card
            Surface(
                color = BrandSurfaceNavy,
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp)
                    .border(1.dp, Color.White.copy(alpha = 0.08f), RoundedCornerShape(20.dp))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = currentQuestion?.text ?: "Soru yükleniyor...",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        textAlign = TextAlign.Center,
                        lineHeight = 28.sp
                    )
                }
            }

            // Options (A, B, C, D)
            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                val optionLetters = listOf("A", "B", "C", "D")
                val options = currentQuestion?.options ?: listOf("", "", "", "")

                options.forEachIndexed { index, optionText ->
                    val isDisabled = matchState.disabledOptionIndices.contains(index)
                    val isSelected = matchState.selectedOptionIndex == index
                    val isCorrect = currentQuestion != null && index == currentQuestion.correctIndex
                    val audiencePercent = matchState.audiencePercentages?.get(index)

                    OptionTile(
                        letter = optionLetters.getOrElse(index) { "$index" },
                        text = optionText,
                        isDisabled = isDisabled,
                        isSelected = isSelected,
                        isAnswerLocked = matchState.isAnswerLocked,
                        isCorrect = isCorrect,
                        audiencePercent = audiencePercent,
                        onClick = {
                            if (!isDisabled && !matchState.isAnswerLocked) {
                                onSelectAnswer(index)
                            }
                        },
                        testTag = "quiz_option_$index"
                    )
                }
            }

            // Bottom Actions: Skip & Powerups
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Skip Button
                OutlinedButton(
                    onClick = onSkipQuestion,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = TextSecondary),
                    modifier = Modifier
                        .height(48.dp)
                        .testTag("skip_question_button")
                ) {
                    Text(
                        text = "SORUYU PAS GEÇ",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = TextSecondary,
                        letterSpacing = 1.sp
                    )
                }

                // Powerups (50/50 and Audience)
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // 50/50 Joker
                    PowerupButton(
                        text = "%50",
                        isUsed = matchState.isFiftyFiftyUsed,
                        onClick = onUseFiftyFifty,
                        testTag = "powerup_fifty_fifty"
                    )

                    // Audience Poll Joker
                    PowerupButton(
                        icon = Icons.Default.Groups,
                        isUsed = matchState.audiencePercentages != null,
                        onClick = onUseAudiencePoll,
                        testTag = "powerup_audience"
                    )
                }
            }
        }
    }
}

@Composable
private fun OptionTile(
    letter: String,
    text: String,
    isDisabled: Boolean,
    isSelected: Boolean,
    isAnswerLocked: Boolean,
    isCorrect: Boolean,
    audiencePercent: Int?,
    onClick: () -> Unit,
    testTag: String
) {
    val bgColor by animateColorAsState(
        targetValue = when {
            isAnswerLocked && isCorrect -> BrandSuccess.copy(alpha = 0.85f)
            isAnswerLocked && isSelected && !isCorrect -> BrandError.copy(alpha = 0.85f)
            isSelected -> BrandPurple
            else -> BrandSurfaceNavy
        },
        animationSpec = tween(300),
        label = "optionBg"
    )

    val borderColor by animateColorAsState(
        targetValue = when {
            isAnswerLocked && isCorrect -> BrandSuccess
            isAnswerLocked && isSelected && !isCorrect -> BrandError
            isSelected -> BrandPurpleLight
            else -> Color.White.copy(alpha = 0.08f)
        },
        label = "optionBorder"
    )

    Surface(
        color = bgColor,
        shape = RoundedCornerShape(14.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(58.dp)
            .alpha(if (isDisabled) 0.25f else 1f)
            .border(1.dp, borderColor, RoundedCornerShape(14.dp))
            .clickable(enabled = !isDisabled && !isAnswerLocked) { onClick() }
            .testTag(testTag)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Circle Letter
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(34.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.1f))
                ) {
                    Text(
                        text = letter,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                Spacer(modifier = Modifier.width(14.dp))

                Text(
                    text = text,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium,
                    color = Color.White
                )
            }

            if (audiencePercent != null) {
                Text(
                    text = "%$audiencePercent",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    color = BrandGold
                )
            }
        }
    }
}

@Composable
private fun PowerupButton(
    text: String? = null,
    icon: androidx.compose.ui.graphics.vector.ImageVector? = null,
    isUsed: Boolean,
    onClick: () -> Unit,
    testTag: String
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(48.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(if (isUsed) Color.Gray.copy(alpha = 0.2f) else BrandPurple.copy(alpha = 0.25f))
            .border(
                1.dp,
                if (isUsed) Color.Gray.copy(alpha = 0.3f) else BrandPurpleLight.copy(alpha = 0.5f),
                RoundedCornerShape(12.dp)
            )
            .clickable(enabled = !isUsed) { onClick() }
            .testTag(testTag)
    ) {
        if (text != null) {
            Text(
                text = text,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = if (isUsed) TextMuted else BrandPurpleLight
            )
        } else if (icon != null) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (isUsed) TextMuted else BrandPurpleLight,
                modifier = Modifier.size(22.dp)
            )
        }
    }
}
