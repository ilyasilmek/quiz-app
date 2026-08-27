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
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.Redeem
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.TaskAlt
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.models.UserProfile
import com.example.data.sample.SampleData
import com.example.ui.components.TriviaBottomNavBar
import com.example.ui.components.TriviaTopBar
import com.example.ui.theme.BrandBgNavy
import com.example.ui.theme.BrandGold
import com.example.ui.theme.BrandPurple
import com.example.ui.theme.BrandPurpleLight
import com.example.ui.theme.BrandSurfaceNavy
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.viewmodel.Screen

@Composable
fun HomeScreen(
    userProfile: UserProfile,
    dailyRewardClaimed: Boolean,
    onClaimDailyReward: () -> Unit,
    onStartMatch: (String) -> Unit,
    onNavigate: (Screen) -> Unit
) {
    var showLeaderboardDialog by remember { mutableStateOf(false) }
    var showQuestsDialog by remember { mutableStateOf(false) }
    var showFriendInviteDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TriviaTopBar(
                userProfile = userProfile,
                onProfileClick = { onNavigate(Screen.PROFILE) }
            )
        },
        bottomBar = {
            TriviaBottomNavBar(
                currentScreen = Screen.HOME,
                onNavigate = onNavigate
            )
        },
        containerColor = BrandBgNavy
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Spacer(modifier = Modifier.height(4.dp))

            // Daily Reward Card
            Surface(
                color = BrandSurfaceNavy,
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, Color.White.copy(alpha = 0.08f), RoundedCornerShape(16.dp))
                    .testTag("daily_reward_card")
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.08f))
                        ) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = "Star",
                                tint = BrandGold,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(14.dp))
                        Column {
                            Text(
                                text = if (dailyRewardClaimed) "Günlük ödül alındı!" else "Günlük ödülünü al!",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            Text(
                                text = if (dailyRewardClaimed) "Yarın tekrar gel" else "100 XP + 50 Altın",
                                style = MaterialTheme.typography.bodyMedium,
                                color = TextSecondary
                            )
                        }
                    }

                    IconButton(
                        onClick = onClaimDailyReward,
                        enabled = !dailyRewardClaimed,
                        modifier = Modifier
                            .size(44.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(if (dailyRewardClaimed) Color.Gray.copy(alpha = 0.3f) else BrandPurple)
                            .testTag("claim_daily_reward_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Redeem,
                            contentDescription = "Claim",
                            tint = Color.White
                        )
                    }
                }
            }

            // "Oyna" (Play) Section
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Oyna",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )

                // Quick Play (Hızlı Oyun - Emerald)
                PlayModeCard(
                    title = "Hızlı Oyun",
                    subtitle = "Rastgele rakiple",
                    badge = "1v1",
                    icon = Icons.Default.RocketLaunch,
                    bgGradient = listOf(Color(0xFF0D3B2E), Color(0xFF08281F)),
                    borderColor = Color(0xFF10B981).copy(alpha = 0.4f),
                    accentColor = Color(0xFF34D399),
                    onClick = { onStartMatch("1v1") },
                    testTag = "play_quick_match"
                )

                // Play with Friend (Arkadaşınla Oyna - Purple)
                PlayModeCard(
                    title = "Arkadaşınla Oyna",
                    subtitle = "Arkadaşını davet et",
                    badge = "1v1",
                    icon = Icons.Default.Group,
                    bgGradient = listOf(Color(0xFF2E1065).copy(alpha = 0.8f), Color(0xFF1A1641)),
                    borderColor = BrandPurple.copy(alpha = 0.6f),
                    accentColor = BrandPurpleLight,
                    onClick = { showFriendInviteDialog = true },
                    testTag = "play_friend_match"
                )

                // Solo Mode (Solo Mod - Amber)
                PlayModeCard(
                    title = "Solo Mod",
                    subtitle = "Kendini geliştir",
                    badge = null,
                    icon = Icons.Default.Person,
                    bgGradient = listOf(Color(0xFF451A03).copy(alpha = 0.6f), Color(0xFF231105)),
                    borderColor = Color(0xFFD97706).copy(alpha = 0.4f),
                    accentColor = Color(0xFFFBBF24),
                    onClick = { onStartMatch("Solo") },
                    testTag = "play_solo_match"
                )
            }

            // "Keşfet" (Explore) Section
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Keşfet",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )

                Surface(
                    color = BrandSurfaceNavy,
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, Color.White.copy(alpha = 0.08f), RoundedCornerShape(16.dp))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        ExploreItemButton(
                            label = "Kategoriler",
                            icon = Icons.Default.Category,
                            tint = BrandPurpleLight,
                            onClick = { onNavigate(Screen.CATEGORIES) },
                            testTag = "explore_categories"
                        )

                        ExploreItemButton(
                            label = "Quizler",
                            icon = Icons.Default.Quiz,
                            tint = BrandPurpleLight,
                            onClick = { onNavigate(Screen.CATEGORIES) },
                            testTag = "explore_quizzes"
                        )

                        ExploreItemButton(
                            label = "Liderlik",
                            icon = Icons.Default.EmojiEvents,
                            tint = BrandGold,
                            onClick = { showLeaderboardDialog = true },
                            testTag = "explore_leaderboard"
                        )

                        ExploreItemButton(
                            label = "Görevler",
                            icon = Icons.Default.TaskAlt,
                            tint = Color(0xFFFF9800),
                            onClick = { showQuestsDialog = true },
                            testTag = "explore_quests"
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }

    // Leaderboard Dialog
    if (showLeaderboardDialog) {
        AlertDialog(
            onDismissRequest = { showLeaderboardDialog = false },
            containerColor = BrandSurfaceNavy,
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.EmojiEvents, contentDescription = null, tint = BrandGold)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Haftalık Liderlik Tablosu", color = TextPrimary, fontWeight = FontWeight.Bold)
                }
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    LeaderboardRow("1", "👑 Mehmet K.", "3.450 P", BrandGold)
                    LeaderboardRow("2", "🥈 Ayşe B.", "2.980 P", Color.White)
                    LeaderboardRow("3", "🥉 Can D.", "2.710 P", Color(0xFFCD7F32))
                    LeaderboardRow("4", "İlyas (Sen)", "1.245 P", BrandPurpleLight, isHighlight = true)
                }
            },
            confirmButton = {
                TextButton(onClick = { showLeaderboardDialog = false }) {
                    Text("Kapat", color = BrandGold)
                }
            }
        )
    }

    // Quests Dialog
    if (showQuestsDialog) {
        AlertDialog(
            onDismissRequest = { showQuestsDialog = false },
            containerColor = BrandSurfaceNavy,
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.TaskAlt, contentDescription = null, tint = Color(0xFFFF9800))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Günlük Görevler", color = TextPrimary, fontWeight = FontWeight.Bold)
                }
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    QuestRow("3 maç tamamla", "2/3", "+150 XP")
                    QuestRow("Tarih kategorisinde 10 doğru yap", "8/10", "+100 XP")
                    QuestRow("1v1 modda bir galibiyet al", "1/1", "Tamamlandı ✨", isCompleted = true)
                }
            },
            confirmButton = {
                TextButton(onClick = { showQuestsDialog = false }) {
                    Text("Tamam", color = BrandGold)
                }
            }
        )
    }

    // Friend Invite Dialog
    if (showFriendInviteDialog) {
        AlertDialog(
            onDismissRequest = { showFriendInviteDialog = false },
            containerColor = BrandSurfaceNavy,
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Group, contentDescription = null, tint = BrandPurpleLight)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Arkadaşınla Oyna", color = TextPrimary, fontWeight = FontWeight.Bold)
                }
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("Oda Kodu: #TRIVIA-882", color = BrandGold, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Text("Arkadaşlarınla bağlantı kurabilir veya listeden davet edebilirsin:", color = TextSecondary, style = MaterialTheme.typography.bodyMedium)
                    SampleData.friends.take(2).forEach { friend ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color.White.copy(alpha = 0.05f), RoundedCornerShape(8.dp))
                                .padding(8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(friend.name, color = TextPrimary, fontWeight = FontWeight.SemiBold)
                            Button(
                                onClick = {
                                    showFriendInviteDialog = false
                                    onStartMatch("Friend")
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = BrandPurple),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text("Meydan Oku", fontSize = 12.sp)
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showFriendInviteDialog = false }) {
                    Text("İptal", color = TextSecondary)
                }
            }
        )
    }
}

@Composable
private fun PlayModeCard(
    title: String,
    subtitle: String,
    badge: String?,
    icon: ImageVector,
    bgGradient: List<Color>,
    borderColor: Color,
    accentColor: Color,
    onClick: () -> Unit,
    testTag: String
) {
    Surface(
        color = Color.Transparent,
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Brush.horizontalGradient(bgGradient))
            .border(1.dp, borderColor, RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .testTag(testTag)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(accentColor.copy(alpha = 0.15f))
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = title,
                        tint = accentColor,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Spacer(modifier = Modifier.width(14.dp))
                Column {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White.copy(alpha = 0.7f)
                    )
                }
            }

            if (badge != null) {
                Box(
                    modifier = Modifier
                        .background(accentColor.copy(alpha = 0.2f), RoundedCornerShape(14.dp))
                        .border(1.dp, accentColor.copy(alpha = 0.4f), RoundedCornerShape(14.dp))
                        .padding(horizontal = 12.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = badge,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = accentColor
                    )
                }
            }
        }
    }
}

@Composable
private fun ExploreItemButton(
    label: String,
    icon: ImageVector,
    tint: Color,
    onClick: () -> Unit,
    testTag: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .clickable { onClick() }
            .padding(8.dp)
            .testTag(testTag)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.05f))
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = tint,
                modifier = Modifier.size(24.dp)
            )
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = TextSecondary
        )
    }
}

@Composable
private fun LeaderboardRow(
    rank: String,
    name: String,
    score: String,
    rankColor: Color,
    isHighlight: Boolean = false
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                if (isHighlight) BrandPurple.copy(alpha = 0.25f) else Color.White.copy(alpha = 0.04f),
                RoundedCornerShape(8.dp)
            )
            .padding(horizontal = 12.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(rank, color = rankColor, fontWeight = FontWeight.Bold, modifier = Modifier.width(24.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(name, color = TextPrimary, fontWeight = FontWeight.Medium)
        }
        Text(score, color = BrandGold, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun QuestRow(
    title: String,
    progress: String,
    reward: String,
    isCompleted: Boolean = false
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White.copy(alpha = 0.04f), RoundedCornerShape(8.dp))
            .padding(10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(title, color = TextPrimary, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium)
            Text(progress, color = TextSecondary, style = MaterialTheme.typography.labelSmall)
        }
        Text(
            reward,
            color = if (isCompleted) BrandGold else Color(0xFF34D399),
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.labelMedium
        )
    }
}
