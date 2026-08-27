package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val TriviaDarkColorScheme = darkColorScheme(
    primary = BrandGold,
    onPrimary = BrandGoldContainer,
    primaryContainer = BrandGoldDim,
    onPrimaryContainer = BrandGoldLight,
    secondary = BrandPurple,
    onSecondary = TextPrimary,
    secondaryContainer = BrandPurpleContainer,
    onSecondaryContainer = BrandPurpleLight,
    tertiary = BrandCyan,
    onTertiary = BrandBgNavy,
    tertiaryContainer = BrandCyan,
    background = BrandBgNavy,
    onBackground = TextPrimary,
    surface = BrandBgNavy,
    onSurface = TextPrimary,
    surfaceVariant = BrandSurfaceNavy,
    onSurfaceVariant = TextSecondary,
    outline = TextMuted,
    outlineVariant = OutlineVariant,
    error = BrandError,
    onError = TextPrimary,
    errorContainer = BrandError
)

@Composable
fun MyApplicationTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = TriviaDarkColorScheme,
        typography = Typography,
        content = content
    )
}
