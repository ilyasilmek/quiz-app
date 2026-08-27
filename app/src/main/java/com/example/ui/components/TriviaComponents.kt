package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.EditNote
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
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
fun TriviaTopBar(
    userProfile: UserProfile,
    onProfileClick: () -> Unit = {}
) {
    Surface(
        color = BrandSurfaceNavy,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // User Avatar & Name
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .clickable { onProfileClick() }
                    .padding(4.dp)
                    .testTag("top_bar_user_profile")
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .border(2.dp, BrandGold, CircleShape)
                ) {
                    AsyncImage(
                        model = userProfile.avatarUrl,
                        contentDescription = "User Avatar",
                        modifier = Modifier.size(40.dp),
                        contentScale = ContentScale.Crop
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = userProfile.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = BrandGold
                    )
                    Text(
                        text = "Seviye ${userProfile.level}",
                        style = MaterialTheme.typography.labelSmall,
                        color = TextSecondary
                    )
                }
            }

            // Trophy score pill
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .background(Color(0xFF1E2020).copy(alpha = 0.6f), RoundedCornerShape(20.dp))
                    .border(1.dp, Color.White.copy(alpha = 0.1f), RoundedCornerShape(20.dp))
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.EmojiEvents,
                    contentDescription = "Score",
                    tint = BrandGold,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "%,d".format(userProfile.totalScore).replace(',', '.'),
                    style = MaterialTheme.typography.labelLarge,
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun TriviaBottomNavBar(
    currentScreen: Screen,
    onNavigate: (Screen) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(84.dp)
    ) {
        // Base Navigation Bar
        Surface(
            color = BrandSurfaceNavy,
            shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(68.dp)
                .align(Alignment.BottomCenter)
                .border(
                    width = 1.dp,
                    color = Color.White.copy(alpha = 0.08f),
                    shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
                )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Home
                NavButton(
                    label = "Ana Sayfa",
                    icon = if (currentScreen == Screen.HOME) Icons.Default.Home else Icons.Outlined.Home,
                    isSelected = currentScreen == Screen.HOME,
                    onClick = { onNavigate(Screen.HOME) },
                    testTag = "nav_home"
                )

                // Explore (Keşfet)
                NavButton(
                    label = "Keşfet",
                    icon = if (currentScreen == Screen.CATEGORIES) Icons.Default.Search else Icons.Outlined.Search,
                    isSelected = currentScreen == Screen.CATEGORIES,
                    onClick = { onNavigate(Screen.CATEGORIES) },
                    testTag = "nav_explore"
                )

                // Center Spacer for Floating Play Button
                Spacer(modifier = Modifier.width(56.dp))

                // Create (Oluştur)
                NavButton(
                    label = "Oluştur",
                    icon = Icons.Outlined.EditNote,
                    isSelected = currentScreen == Screen.CREATE_QUIZ || currentScreen == Screen.AI_GENERATOR,
                    onClick = { onNavigate(Screen.AI_GENERATOR) },
                    testTag = "nav_create"
                )

                // Profile (Profil)
                NavButton(
                    label = "Profil",
                    icon = if (currentScreen == Screen.PROFILE) Icons.Default.Person else Icons.Outlined.Person,
                    isSelected = currentScreen == Screen.PROFILE || currentScreen == Screen.STATISTICS,
                    onClick = { onNavigate(Screen.PROFILE) },
                    testTag = "nav_profile"
                )
            }
        }

        // Center Floating Play Action Button
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .offset(y = (-4).dp)
                .size(60.dp)
                .shadow(16.dp, CircleShape, spotColor = BrandPurple)
                .clip(CircleShape)
                .background(
                    Brush.radialGradient(
                        colors = listOf(BrandPurpleLight, BrandPurple)
                    )
                )
                .clickable { onNavigate(Screen.HOME) }
                .testTag("nav_play_fab")
        ) {
            Icon(
                imageVector = Icons.Default.Bolt,
                contentDescription = "Hızlı Oyna",
                tint = Color.White,
                modifier = Modifier.size(34.dp)
            )
        }
    }
}

@Composable
private fun NavButton(
    label: String,
    icon: ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit,
    testTag: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .clickable { onClick() }
            .padding(horizontal = 8.dp, vertical = 6.dp)
            .testTag(testTag)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = if (isSelected) BrandGold else TextMuted,
            modifier = Modifier.size(22.dp)
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = label,
            fontSize = 10.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            color = if (isSelected) BrandGold else TextMuted
        )
    }
}
