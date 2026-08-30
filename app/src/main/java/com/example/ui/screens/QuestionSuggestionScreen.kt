package com.example.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.ui.theme.BrandBgNavy
import com.example.ui.theme.BrandPurple
import com.example.ui.theme.BrandPurpleLight
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun QuestionSuggestionScreen(
    category: String,
    question: String,
    options: List<String>,
    correctIndex: Int,
    explanation: String,
    source: String,
    submitted: Boolean,
    isSubmitting: Boolean,
    submissionError: String?,
    onCategoryChange: (String) -> Unit,
    onQuestionChange: (String) -> Unit,
    onOptionChange: (Int, String) -> Unit,
    onCorrectChange: (Int) -> Unit,
    onExplanationChange: (String) -> Unit,
    onSourceChange: (String) -> Unit,
    onSubmit: () -> Unit,
    onReset: () -> Unit,
    onBack: () -> Unit
) {
    val valid = question.trim().length >= 10 &&
        options.all { it.trim().isNotEmpty() } &&
        options.distinct().size == 4

    Scaffold(containerColor = BrandBgNavy) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Spacer(Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedButton(onClick = onBack) { Text("Geri") }
                Text(
                    "Soru Öner",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
            }

            if (submitted) {
                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = BrandPurpleLight)
                Text("Sorun onaya gönderildi.", color = TextPrimary, fontWeight = FontWeight.Bold)
                Text(
                    "Otomatik tekrar ve kalite kontrollerinden geçecek. Ardından admin tarafından soru ve doğru cevap teyit edilecek.",
                    color = TextSecondary
                )
                Button(
                    onClick = onReset,
                    colors = ButtonDefaults.buttonColors(containerColor = BrandPurple)
                ) { Text("Yeni Soru Öner") }
            } else {
                Icon(Icons.Default.Lightbulb, contentDescription = null, tint = BrandPurpleLight)
                Text(
                    "Bildiklerini oyuna kazandır.",
                    color = TextPrimary,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    "Gönderdiğin soru hemen yayınlanmaz. Admin incelemesi ve tekrar kontrolünden sonra uygun görülürse yayınlanır.",
                    color = TextSecondary
                )

                OutlinedTextField(
                    value = category,
                    onValueChange = onCategoryChange,
                    label = { Text("Kategori") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = question,
                    onValueChange = onQuestionChange,
                    label = { Text("Soru") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3
                )

                options.forEachIndexed { index, option ->
                    Row(modifier = Modifier.fillMaxWidth()) {
                        RadioButton(selected = correctIndex == index, onClick = { onCorrectChange(index) })
                        OutlinedTextField(
                            value = option,
                            onValueChange = { onOptionChange(index, it) },
                            label = { Text("Şık " + (index + 1)) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                OutlinedTextField(
                    value = explanation,
                    onValueChange = onExplanationChange,
                    label = { Text("Açıklama (önerilir)") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 2
                )
                OutlinedTextField(
                    value = source,
                    onValueChange = onSourceChange,
                    label = { Text("Kaynak / referans (önerilir)") },
                    modifier = Modifier.fillMaxWidth()
                )

                submissionError?.let {
                    Text(it, color = MaterialTheme.colorScheme.error)
                }

                Button(
                    onClick = onSubmit,
                    enabled = valid && !isSubmitting,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = BrandPurple)
                ) {
                    Text(if (isSubmitting) "Gönderiliyor..." else "Onaya Gönder")
                }
            }
            Spacer(Modifier.height(24.dp))
        }
    }
}
