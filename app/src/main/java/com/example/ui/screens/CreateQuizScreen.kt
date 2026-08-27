package com.example.ui.screens

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.models.Question
import com.example.ui.theme.BrandBgNavy
import com.example.ui.theme.BrandError
import com.example.ui.theme.BrandGold
import com.example.ui.theme.BrandGoldLight
import com.example.ui.theme.BrandPurple
import com.example.ui.theme.BrandPurpleLight
import com.example.ui.theme.BrandSuccess
import com.example.ui.theme.BrandSurfaceNavy
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.viewmodel.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateQuizScreen(
    step: Int,
    currentQuestion: Question,
    draftCount: Int,
    onStepChange: (Int) -> Unit,
    onQuestionTextChange: (String) -> Unit,
    onOptionChange: (Int, String) -> Unit,
    onCorrectOptionChange: (Int) -> Unit,
    onAddQuestion: () -> Unit,
    onPublish: () -> Unit,
    onSwitchToAi: () -> Unit,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = "Quiz Oluştur",
                            style = MaterialTheme.typography.headlineMedium,
                            color = BrandGoldLight,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier.testTag("create_quiz_back_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Geri",
                            tint = TextPrimary
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = onSwitchToAi,
                        modifier = Modifier.testTag("switch_to_ai_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = "AI ile Oluştur",
                            tint = BrandGold
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = BrandBgNavy)
            )
        },
        containerColor = BrandBgNavy
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .testTag("create_quiz_screen")
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp)
                    .verticalScroll(rememberScrollState())
                    .padding(bottom = 90.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Stepper Header (1 Bilgi -> 2 Sorular -> 3 Yayınla)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    StepItem(number = "1", label = "Bilgi", isActive = step >= 1, isCurrent = step == 1)
                    StepDivider(isActive = step >= 2)
                    StepItem(number = "2", label = "Sorular", isActive = step >= 2, isCurrent = step == 2)
                    StepDivider(isActive = step >= 3)
                    StepItem(number = "3", label = "Yayınla", isActive = step >= 3, isCurrent = step == 3)
                }

                // Soru Card (Soru 1, Soru Başlığı text area)
                Surface(
                    color = BrandSurfaceNavy,
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, Color.White.copy(alpha = 0.08f), RoundedCornerShape(16.dp))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Soru ${draftCount + 1}",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = BrandPurpleLight
                            )

                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Sil",
                                tint = BrandError.copy(alpha = 0.8f),
                                modifier = Modifier
                                    .size(20.dp)
                                    .clickable { onQuestionTextChange("") }
                            )
                        }

                        OutlinedTextField(
                            value = currentQuestion.text,
                            onValueChange = onQuestionTextChange,
                            placeholder = { Text("Sorunuzu buraya yazın...", color = TextMuted) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(90.dp)
                                .testTag("create_quiz_question_input"),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = BrandPurple,
                                unfocusedBorderColor = Color.White.copy(alpha = 0.1f),
                                focusedTextColor = TextPrimary,
                                unfocusedTextColor = TextPrimary,
                                focusedContainerColor = Color.White.copy(alpha = 0.03f),
                                unfocusedContainerColor = Color.White.copy(alpha = 0.03f)
                            ),
                            shape = RoundedCornerShape(12.dp)
                        )

                        // Şıklar Section
                        Text(
                            text = "Şıklar (Doğru cevabı seçin)",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = TextSecondary
                        )

                        val optionLabels = listOf("A", "B", "C", "D")
                        optionLabels.forEachIndexed { index, letter ->
                            val optionText = currentQuestion.options.getOrElse(index) { "" }
                            val isCorrect = currentQuestion.correctIndex == index

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Option Letter Box
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(if (isCorrect) BrandSuccess.copy(alpha = 0.2f) else Color.White.copy(alpha = 0.06f))
                                        .border(1.dp, if (isCorrect) BrandSuccess else Color.White.copy(alpha = 0.1f), RoundedCornerShape(8.dp))
                                ) {
                                    Text(
                                        text = letter,
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isCorrect) BrandSuccess else TextPrimary
                                    )
                                }

                                Spacer(modifier = Modifier.width(8.dp))

                                // Option Input
                                OutlinedTextField(
                                    value = optionText,
                                    onValueChange = { onOptionChange(index, it) },
                                    placeholder = { Text("Seçenek $letter", color = TextMuted) },
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(52.dp)
                                        .testTag("create_quiz_option_$index"),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = BrandPurple,
                                        unfocusedBorderColor = Color.White.copy(alpha = 0.1f),
                                        focusedTextColor = TextPrimary,
                                        unfocusedTextColor = TextPrimary,
                                        focusedContainerColor = Color.White.copy(alpha = 0.03f),
                                        unfocusedContainerColor = Color.White.copy(alpha = 0.03f)
                                    ),
                                    shape = RoundedCornerShape(10.dp)
                                )

                                Spacer(modifier = Modifier.width(8.dp))

                                // Correct Toggle Indicator
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clip(CircleShape)
                                        .background(if (isCorrect) BrandSuccess else Color.White.copy(alpha = 0.08f))
                                        .clickable { onCorrectOptionChange(index) }
                                ) {
                                    Icon(
                                        imageVector = if (isCorrect) Icons.Default.Check else Icons.Default.Close,
                                        contentDescription = "Doğru Şık",
                                        tint = if (isCorrect) Color.White else TextMuted,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                // Add Question Button
                OutlinedButton(
                    onClick = onAddQuestion,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("add_question_button"),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = BrandPurpleLight)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Add, contentDescription = null, tint = BrandPurpleLight)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "+ Soru Ekle",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = BrandPurpleLight
                        )
                    }
                }
            }

            // Fixed Bottom Proceed Button
            Surface(
                color = BrandBgNavy.copy(alpha = 0.95f),
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Button(
                    onClick = onPublish,
                    colors = ButtonDefaults.buttonColors(containerColor = BrandPurple),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp)
                        .testTag("create_quiz_proceed_button")
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Devam Et",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = null,
                            tint = Color.White
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun StepItem(
    number: String,
    label: String,
    isActive: Boolean,
    isCurrent: Boolean
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(28.dp)
                .clip(CircleShape)
                .background(if (isActive) BrandPurple else Color.White.copy(alpha = 0.1f))
                .then(
                    if (isCurrent) Modifier.border(2.dp, BrandGold, CircleShape) else Modifier
                )
        ) {
            Text(
                text = number,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Normal,
            color = if (isActive) TextPrimary else TextMuted
        )
    }
}

@Composable
private fun StepDivider(isActive: Boolean) {
    Box(
        modifier = Modifier
            .width(28.dp)
            .height(2.dp)
            .background(if (isActive) BrandPurple else Color.White.copy(alpha = 0.1f))
    )
}
