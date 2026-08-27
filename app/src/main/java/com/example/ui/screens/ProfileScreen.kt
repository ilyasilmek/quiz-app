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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.models.UserProfile
import com.example.data.sample.SampleData
import com.example.ui.components.TriviaBottomNavBar
import com.example.ui.theme.BrandBgNavy
import com.example.ui.theme.BrandGold
import com.example.ui.theme.BrandPurple
import com.example.ui.theme.BrandPurpleLight
import com.example.ui.theme.BrandSurfaceNavy
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.viewmodel.Screen

@Composable
fun ProfileScreen(
    userProfile: UserProfile,
    onNavigate: (Screen) -> Unit
) {
    var showAchievementsDialog by remember { mutableStateOf(false) }
    var showFriendsDialog by remember { mutableStateOf(false) }
    var showMyQuizzesDialog by remember { mutableStateOf(false) }

    val xpFraction = (userProfile.currentXp.toFloat() / userProfile.maxXp.toFloat()).coerceIn(0f, 1f)

    Scaffold(
        bottomBar = {
            TriviaBottomNavBar(
                currentScreen = Screen.PROFILE,
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
                .verticalScroll(rememberScrollState())
                .padding(bottom = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            Spacer(modifier = Modifier.height(12.dp))

            // Profile Header (Avatar with Level Badge, Name, Edit)
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    contentAlignment = Alignment.BottomEnd,
                    modifier = Modifier.size(96.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(96.dp)
                            .clip(CircleShape)
                            .border(3.dp, BrandGold, CircleShape)
                            .padding(3.dp)
                    ) {
                        AsyncImage(
                            model = userProfile.avatarUrl,
                            contentDescription = "Avatar",
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                    }

                    // Level 12 badge
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .offset(x = 4.dp, y = 4.dp)
                            .size(28.dp)
                            .clip(CircleShape)
                            .background(BrandPurple)
                            .border(1.5.dp, Color.White, CircleShape)
                    ) {
                        Text(
                            text = "${userProfile.level}",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = userProfile.name,
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Düzenle",
                        tint = TextMuted,
                        modifier = Modifier.size(18.dp)
                    )
                }

                Text(
                    text = "Seviye ${userProfile.level}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = BrandGold
                )
            }

            // XP Progress Card
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
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Seviye İlerlemesi",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = "%,d / %,d XP".format(userProfile.currentXp, userProfile.maxXp).replace(',', '.'),
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = BrandPurpleLight
                        )
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(Color.White.copy(alpha = 0.08f))
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(xpFraction)
                                .height(8.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .background(
                                    Brush.horizontalGradient(
                                        listOf(BrandPurple, BrandPurpleLight, BrandGold)
                                    )
                                )
                        )
                    }
                }
            }

            // Bento Stats Grid (Toplam Puan, Maç, Kazanma Oranı)
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
                        .padding(18.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    ProfileBentoStat("TOPLAM PUAN", "%,d".format(userProfile.totalScore).replace(',', '.'), BrandGold)
                    ProfileBentoStat("MAÇ", "${userProfile.matchCount}", TextPrimary)
                    ProfileBentoStat("KAZANMA ORANI", "%${userProfile.winRate}", TextPrimary)
                }
            }

            // Menu List Items
            Surface(
                color = BrandSurfaceNavy,
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, Color.White.copy(alpha = 0.08f), RoundedCornerShape(16.dp))
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    ProfileMenuItem(
                        label = "İstatistikler",
                        icon = Icons.Default.BarChart,
                        trailing = "",
                        onClick = { onNavigate(Screen.STATISTICS) },
                        testTag = "profile_menu_stats"
                    )

                    ProfileMenuItem(
                        label = "Başarımlar",
                        icon = Icons.Default.EmojiEvents,
                        trailing = "12/45",
                        onClick = { showAchievementsDialog = true },
                        testTag = "profile_menu_achievements"
                    )

                    ProfileMenuItem(
                        label = "Arkadaşlar",
                        icon = Icons.Default.Group,
                        trailing = "32",
                        onClick = { showFriendsDialog = true },
                        testTag = "profile_menu_friends"
                    )

                    ProfileMenuItem(
                        label = "Oluşturduğum Quizler",
                        icon = Icons.Default.Quiz,
                        trailing = "8",
                        onClick = { showMyQuizzesDialog = true },
                        testTag = "profile_menu_my_quizzes"
                    )
                }
            }
        }
    }

    // Achievements Dialog
    if (showAchievementsDialog) {
        AlertDialog(
            onDismissRequest = { showAchievementsDialog = false },
            containerColor = BrandSurfaceNavy,
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.EmojiEvents, contentDescription = null, tint = BrandGold)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Başarımlar (12/45)", color = TextPrimary, fontWeight = FontWeight.Bold)
                }
            },
            text = {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.height(260.dp)) {
                    items(SampleData.achievements) { item ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color.White.copy(alpha = 0.05f), RoundedCornerShape(8.dp))
                                .padding(10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(item.title, color = TextPrimary, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyMedium)
                                Text(item.description, color = TextSecondary, style = MaterialTheme.typography.labelSmall)
                            }
                            Text(
                                if (item.isUnlocked) "Kazanıldı ✨" else "${item.progress}/${item.maxProgress}",
                                color = if (item.isUnlocked) BrandGold else TextMuted,
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showAchievementsDialog = false }) {
                    Text("Tamam", color = BrandGold)
                }
            }
        )
    }

    // Friends Dialog
    if (showFriendsDialog) {
        AlertDialog(
            onDismissRequest = { showFriendsDialog = false },
            containerColor = BrandSurfaceNavy,
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Group, contentDescription = null, tint = BrandPurpleLight)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Arkadaşlar (32)", color = TextPrimary, fontWeight = FontWeight.Bold)
                }
            },
            text = {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.height(240.dp)) {
                    items(SampleData.friends) { friend ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color.White.copy(alpha = 0.05f), RoundedCornerShape(8.dp))
                                .padding(8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clip(CircleShape)
                                        .border(1.5.dp, if (friend.isOnline) BrandPurpleLight else Color.Gray, CircleShape)
                                ) {
                                    AsyncImage(model = friend.avatarUrl, contentDescription = null, modifier = Modifier.fillMaxSize(), contentScale = ContentScale.Crop)
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(friend.name, color = TextPrimary, fontWeight = FontWeight.Medium, style = MaterialTheme.typography.bodyMedium)
                                    Text("Seviye ${friend.level}", color = TextSecondary, style = MaterialTheme.typography.labelSmall)
                                }
                            }
                            Text(if (friend.isOnline) "Çevrimiçi" else "Çevrimdışı", color = if (friend.isOnline) Color(0xFF34D399) else TextMuted, fontSize = 11.sp)
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showFriendsDialog = false }) {
                    Text("Kapat", color = BrandGold)
                }
            }
        )
    }

    // My Quizzes Dialog
    if (showMyQuizzesDialog) {
        AlertDialog(
            onDismissRequest = { showMyQuizzesDialog = false },
            containerColor = BrandSurfaceNavy,
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Quiz, contentDescription = null, tint = BrandPurpleLight)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Oluşturduğum Quizler (8)", color = TextPrimary, fontWeight = FontWeight.Bold)
                }
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("1. Osmanlı Devleti Duraklama Dönemi (10 Soru - 2.340 Oynanma)", color = TextPrimary, style = MaterialTheme.typography.bodyMedium)
                    Text("2. Türkiye Coğrafyası ve Başkentler (8 Soru - 1.820 Oynanma)", color = TextPrimary, style = MaterialTheme.typography.bodyMedium)
                    Text("3. Genel Bilim ve Fizik Kuralları (5 Soru - 950 Oynanma)", color = TextPrimary, style = MaterialTheme.typography.bodyMedium)
                }
            },
            confirmButton = {
                TextButton(onClick = { showMyQuizzesDialog = false }) {
                    Text("Tamam", color = BrandGold)
                }
            }
        )
    }
}

@Composable
private fun ProfileBentoStat(
    label: String,
    value: String,
    valueColor: Color
) {
    Column(
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = TextSecondary,
            letterSpacing = 1.sp
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = valueColor
        )
    }
}

@Composable
private fun ProfileMenuItem(
    label: String,
    icon: ImageVector,
    trailing: String,
    onClick: () -> Unit,
    testTag: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 14.dp)
            .testTag(testTag),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = BrandPurpleLight,
                modifier = Modifier.size(22.dp)
            )
            Spacer(modifier = Modifier.width(14.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium,
                color = TextPrimary
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (trailing.isNotBlank()) {
                Text(
                    text = trailing,
                    style = MaterialTheme.typography.labelMedium,
                    color = TextSecondary
                )
                Spacer(modifier = Modifier.width(6.dp))
            }
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = null,
                tint = TextMuted,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}
