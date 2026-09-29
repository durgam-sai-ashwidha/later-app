package com.example.ui.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.LaterLogoMark
import com.example.ui.theme.LaterDarkAccent
import com.example.ui.theme.LaterInkPrimary
import com.example.ui.theme.LaterPaperBg
import com.example.ui.theme.LaterTerracotta
import com.example.ui.theme.PlusJakartaSansFamily
import kotlinx.coroutines.delay

/**
 * 4. Minimal LATER Splash Screen:
 * Warm paper background, centered compact logo, LATER, and small "WHY FIRST".
 * Subtle, quiet, unhurried — no loading gimmicks, particles, or AI animations.
 */
@Composable
fun SplashScreen(
    onSplashComplete: () -> Unit
) {
    val alphaAnim = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        alphaAnim.animateTo(1f, animationSpec = tween(400))
        delay(700)
        onSplashComplete()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(LaterPaperBg),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.alpha(alphaAnim.value)
        ) {
            LaterLogoMark(
                size = 46.dp,
                tint = LaterTerracotta,
                foldTint = LaterDarkAccent,
                cutoutColor = LaterPaperBg
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "LATER",
                fontFamily = PlusJakartaSansFamily,
                fontSize = 28.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 3.sp,
                color = LaterInkPrimary
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "WHY FIRST",
                fontFamily = PlusJakartaSansFamily,
                fontSize = 13.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 2.sp,
                color = LaterTerracotta
            )
        }
    }
}
