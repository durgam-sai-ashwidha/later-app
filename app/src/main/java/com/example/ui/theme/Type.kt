package com.example.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.R

// Custom Google Fonts bundled locally in res/font
val NewsreaderFamily = FontFamily(
    Font(R.font.newsreader, FontWeight.Normal),
    Font(R.font.newsreader, FontWeight.Medium),
    Font(R.font.newsreader, FontWeight.Bold)
)

val PlusJakartaSansFamily = FontFamily(
    Font(R.font.plus_jakarta_sans, FontWeight.Normal),
    Font(R.font.plus_jakarta_sans, FontWeight.Medium),
    Font(R.font.plus_jakarta_sans, FontWeight.SemiBold),
    Font(R.font.plus_jakarta_sans, FontWeight.Bold)
)

val LaterSerif = NewsreaderFamily
val LaterSans = PlusJakartaSansFamily

// High-legibility, editorial Material 3 Typography system
val Typography = Typography(
    displayLarge = TextStyle(
        fontFamily = NewsreaderFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 30.sp,
        lineHeight = 38.sp,
        letterSpacing = (-0.4).sp,
        color = LaterInkPrimary
    ),
    headlineLarge = TextStyle(
        fontFamily = NewsreaderFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 25.sp,
        lineHeight = 32.sp,
        letterSpacing = (-0.2).sp,
        color = LaterInkPrimary
    ),
    headlineMedium = TextStyle(
        fontFamily = NewsreaderFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 21.sp,
        lineHeight = 28.sp,
        color = LaterInkPrimary
    ),
    headlineSmall = TextStyle(
        fontFamily = NewsreaderFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 18.5.sp,
        lineHeight = 25.sp,
        color = LaterInkPrimary
    ),
    titleLarge = TextStyle(
        fontFamily = PlusJakartaSansFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp,
        lineHeight = 25.sp,
        letterSpacing = 0.sp,
        color = LaterInkPrimary
    ),
    titleMedium = TextStyle(
        fontFamily = PlusJakartaSansFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 15.5.sp,
        lineHeight = 22.sp,
        letterSpacing = 0.15.sp,
        color = LaterInkPrimary
    ),
    titleSmall = TextStyle(
        fontFamily = PlusJakartaSansFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 13.5.sp,
        lineHeight = 19.sp,
        letterSpacing = 0.1.sp,
        color = LaterInkPrimary
    ),
    bodyLarge = TextStyle(
        fontFamily = PlusJakartaSansFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 25.sp,
        letterSpacing = 0.15.sp,
        color = LaterInkPrimary
    ),
    bodyMedium = TextStyle(
        fontFamily = PlusJakartaSansFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 14.5.sp,
        lineHeight = 22.sp,
        letterSpacing = 0.2.sp,
        color = LaterSecondaryText
    ),
    bodySmall = TextStyle(
        fontFamily = PlusJakartaSansFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 13.sp,
        lineHeight = 18.sp,
        letterSpacing = 0.2.sp,
        color = LaterSecondaryText
    ),
    labelLarge = TextStyle(
        fontFamily = PlusJakartaSansFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp,
        lineHeight = 19.sp,
        letterSpacing = 0.8.sp
    ),
    labelMedium = TextStyle(
        fontFamily = PlusJakartaSansFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 13.sp,
        lineHeight = 18.sp,
        letterSpacing = 1.sp
    ),
    labelSmall = TextStyle(
        fontFamily = PlusJakartaSansFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.8.sp
    )
)
