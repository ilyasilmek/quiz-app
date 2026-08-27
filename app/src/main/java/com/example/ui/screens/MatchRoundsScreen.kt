package com.example.ui.screens

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.HourglassEmpty
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.models.MatchRoundStatus
import com.example.data.models.MatchState
import com.example.data.models.RoundAnswerStatus
import com.example.data.models.UserProfile
import com.example.ui.theme.BrandBgNavy
import com.example.ui.theme.BrandCyan
import com.example.ui.theme.BrandError
import com.example.ui.theme.BrandGold
import com.example.ui.theme.BrandPurple
import com.example.ui.theme.BrandSuccess
import com.example.ui.theme.BrandSurfaceNavy
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun MatchRoundsScreen(
    userProfile: UserProfile,
    matchState: MatchState,
    onProceedToQuiz: () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseAlpha"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BrandBgNavy)
            .clickable { onProceedToQuiz() }
            .testTag("match_rounds_screen")
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header: Round Progress & Countdown
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.width(40.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Timer,
                        contentDescription = "Zamanlayıcı",
                        tint = BrandGold,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "${matchState.currentQuestionIndex + 1} / ${matchState.quiz.questions.size}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                }

                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(BrandSurfaceNavy)
                        .border(2.dp, BrandGold, CircleShape)
                ) {
                    Text(
                        text = "${matchState.timeRemaining}",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        color = BrandGold
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Player vs Opponent Section
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Player (Sen)
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(74.dp)
                                .clip(CircleShape)
                                .border(3.dp, BrandCyan, CircleShape)
                                .padding(3.dp)
                        ) {
                            AsyncImage(
                                model = userProfile.avatarUrl,
                                contentDescription = "Sen",
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(CircleShape),
                                contentScale = ContentScale.Crop
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .background(BrandSurfaceNavy, RoundedCornerShape(12.dp))
                                .border(1.dp, BrandCyan.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                                .padding(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Icon(Icons.Default.EmojiEvents, contentDescription = null, tint = BrandCyan, modifier = Modifier.size(12.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("1.240", fontSize = 11.sp, color = TextPrimary, fontWeight = FontWeight.Bold)
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Sen",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                    }

                    // Opponent (Rakip)
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(74.dp)
                                .clip(CircleShape)
                                .border(3.dp, BrandError, CircleShape)
                                .padding(3.dp)
                        ) {
                            AsyncImage(
                                model = matchState.opponentAvatar,
                                contentDescription = "Rakip",
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(CircleShape),
                                contentScale = ContentScale.Crop
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .background(BrandSurfaceNavy, RoundedCornerShape(12.dp))
                                .border(1.dp, BrandError.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                                .padding(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Icon(Icons.Default.EmojiEvents, contentDescription = null, tint = BrandGold, modifier = Modifier.size(12.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("1.180", fontSize = 11.sp, color = TextPrimary, fontWeight = FontWeight.Bold)
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = matchState.opponentName,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = TextSecondary
                        )
                    }
                }

                // VS Badge in Center
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(56.dp)
                        .shadow(16.dp, RoundedCornerShape(16.dp), spotColor = BrandPurple)
                        .clip(RoundedCornerShape(16.dp))
                        .background(BrandSurfaceNavy)
                        .border(1.dp, BrandPurple, RoundedCornerShape(16.dp))
                ) {
                    Text(
                        text = "VS",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Black,
                        color = BrandGold,
                        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                    )
                }
            }

            // Rounds List Card
            Surface(
                color = BrandSurfaceNavy,
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .border(1.dp, Color.White.copy(alpha = 0.08f), RoundedCornerShape(16.dp))
            ) {
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    // Header status
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.White.copy(alpha = 0.03f))
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(CircleShape)
                                    .background(BrandCyan)
                                    .alpha(pulseAlpha)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Cevaplıyor...",
                                style = MaterialTheme.typography.labelSmall,
                                color = TextSecondary
                            )
                        }

                        Icon(
                            imageVector = Icons.Default.HourglassEmpty,
                            contentDescription = null,
                            tint = BrandGold,
                            modifier = Modifier.size(16.dp)
                        )
                    }

                    // List of rounds
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(matchState.roundsStatus) { round ->
                            val isCurrent = round.roundNumber == matchState.currentQuestionIndex + 1
                            RoundStatusRow(
                                round = round,
                                isCurrent = isCurrent
                            )
                        }
                    }
                }
            }

            // Touch to continue hint
            Text(
                text = "Devam etmek için ekrana dokunun",
                style = MaterialTheme.typography.labelSmall,
                color = TextMuted,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }
}

@Composable
private fun RoundStatusRow(
    round: MatchRoundStatus,
    isCurrent: Boolean
) {
    Surface(
        color = if (isCurrent) Color.White.copy(alpha = 0.06f) else Color.Transparent,
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .then(
                if (isCurrent) Modifier.border(1.dp, BrandCyan.copy(alpha = 0.4f), RoundedCornerShape(8.dp))
                else Modifier
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Player side icon
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.width(48.dp)
            ) {
                when (round.senStatus) {
                    RoundAnswerStatus.CORRECT -> IconCircle(Icons.Default.Check, BrandSuccess)
                    RoundAnswerStatus.WRONG -> IconCircle(Icons.Default.Close, BrandError)
                    RoundAnswerStatus.THINKING -> Text("Cevaplıyor..", fontSize = 9.sp, color = TextSecondary)
                    RoundAnswerStatus.NONE -> {
                        if (isCurrent) Text("Cevaplıyor..", fontSize = 9.sp, color = TextSecondary)
                        else DotCircle()
                    }
                }
            }

            // Round Number
            Text(
                text = "${round.roundNumber}",
                style = if (isCurrent) MaterialTheme.typography.titleMedium else MaterialTheme.typography.bodyMedium,
                fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Normal,
                color = if (isCurrent) TextPrimary else TextMuted
            )

            // Opponent side icon
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.width(48.dp)
            ) {
                when (round.rakipStatus) {
                    RoundAnswerStatus.CORRECT -> IconCircle(Icons.Default.Check, BrandSuccess)
                    RoundAnswerStatus.WRONG -> IconCircle(Icons.Default.Close, BrandError)
                    RoundAnswerStatus.THINKING -> Icon(Icons.Default.HourglassEmpty, contentDescription = null, tint = BrandGold, modifier = Modifier.size(16.dp))
                    RoundAnswerStatus.NONE -> {
                        if (isCurrent) Icon(Icons.Default.HourglassEmpty, contentDescription = null, tint = BrandGold, modifier = Modifier.size(16.dp))
                        else DotCircle()
                    }
                }
            }
        }
    }
}

@Composable
private fun IconCircle(icon: androidx.compose.ui.graphics.vector.ImageVector, color: Color) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(24.dp)
            .clip(CircleShape)
            .background(color.copy(alpha = 0.2f))
            .border(1.dp, color, CircleShape)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = color,
            modifier = Modifier.size(14.dp)
        )
    }
}

@Composable
private fun DotCircle() {
    Box(
        modifier = Modifier
            .size(6.dp)
            .clip(CircleShape)
            .background(TextMuted.copy(alpha = 0.4f))
    )
}
