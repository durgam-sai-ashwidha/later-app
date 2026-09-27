package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = LaterTerracotta,
    onPrimary = Color.White,
    primaryContainer = Color(0xFF332019),
    onPrimaryContainer = Color(0xFFFFDBCF),
    secondary = LaterDarkWarmGray,
    onSecondary = LaterDarkPaperBg,
    background = LaterDarkPaperBg,
    onBackground = LaterDarkInkPrimary,
    surface = LaterDarkCardBg,
    onSurface = LaterDarkInkPrimary,
    surfaceVariant = Color(0xFF282823),
    onSurfaceVariant = LaterDarkWarmGray,
    outline = LaterDarkBorder
)

private val LightColorScheme = lightColorScheme(
    primary = LaterTerracotta,
    onPrimary = Color.White,
    primaryContainer = LaterWhyBg,
    onPrimaryContainer = LaterTerracotta,
    secondary = LaterWarmGray,
    onSecondary = Color.White,
    background = LaterPaperBg,
    onBackground = LaterInkPrimary,
    surface = LaterCardBg,
    onSurface = LaterInkPrimary,
    surfaceVariant = Color(0xFFEDE8DE),
    onSurfaceVariant = LaterWarmGray,
    outline = LaterBorder
)

@Composable
fun LaterTheme(
    darkTheme: Boolean = false, // Default to light paper archive aesthetic
    dynamicColor: Boolean = false, // Preserve editorial paper aesthetic
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = false,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    LaterTheme(darkTheme = darkTheme, dynamicColor = dynamicColor, content = content)
}
