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
        fontSize = 34.sp,
        lineHeight = 42.sp,
        letterSpacing = (-0.5).sp,
        color = LaterInkPrimary
    ),
    headlineLarge = TextStyle(
        fontFamily = NewsreaderFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 28.sp,
        lineHeight = 36.sp,
        letterSpacing = (-0.3).sp,
        color = LaterInkPrimary
    ),
    headlineMedium = TextStyle(
        fontFamily = NewsreaderFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 22.sp,
        lineHeight = 30.sp,
        color = LaterInkPrimary
    ),
    headlineSmall = TextStyle(
        fontFamily = NewsreaderFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 19.sp,
        lineHeight = 26.sp,
        color = LaterInkPrimary
    ),
    titleLarge = TextStyle(
        fontFamily = PlusJakartaSansFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp,
        lineHeight = 26.sp,
        letterSpacing = 0.sp,
        color = LaterInkPrimary
    ),
    titleMedium = TextStyle(
        fontFamily = PlusJakartaSansFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 15.sp,
        lineHeight = 22.sp,
        letterSpacing = 0.15.sp,
        color = LaterInkPrimary
    ),
    titleSmall = TextStyle(
        fontFamily = PlusJakartaSansFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 13.sp,
        lineHeight = 18.sp,
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
        fontSize = 14.sp,
        lineHeight = 22.sp,
        letterSpacing = 0.25.sp,
        color = LaterWarmGray
    ),
    bodySmall = TextStyle(
        fontFamily = PlusJakartaSansFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 18.sp,
        letterSpacing = 0.3.sp,
        color = LaterTextMuted
    ),
    labelLarge = TextStyle(
        fontFamily = PlusJakartaSansFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 13.sp,
        lineHeight = 18.sp,
        letterSpacing = 0.8.sp
    ),
    labelMedium = TextStyle(
        fontFamily = PlusJakartaSansFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 1.2.sp
    ),
    labelSmall = TextStyle(
        fontFamily = PlusJakartaSansFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 10.sp,
        lineHeight = 14.sp,
        letterSpacing = 1.sp
    )
)
