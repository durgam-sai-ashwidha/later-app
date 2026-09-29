package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.LaterDarkAccent
import com.example.ui.theme.LaterInkPrimary
import com.example.ui.theme.LaterPaperBg
import com.example.ui.theme.LaterTerracotta
import com.example.ui.theme.PlusJakartaSansFamily

/**
 * 2. LATER LOGO SYSTEM
 *
 * Core Concept:
 * A terracotta geometric L/bookmark/page-corner symbol.
 * - Simple, geometric, instantly recognizable at tiny icon scales.
 * - Visually connects "save now → find later".
 * - Symbol works completely independently from the word LATER.
 *
 * Variants:
 * A. Primary:  Terracotta symbol + LATER wordmark on warm paper
 * B. Compact:  Symbol only
 * C. Dark:     Ink symbol on warm paper
 * D. Reversed: Cream symbol on dark ink
 */

/**
 * Base Geometric Symbol for LATER:
 * A folded page corner / bookmark silhouette enclosing the architectural letter "L".
 */
@Composable
fun LaterLogoMark(
    modifier: Modifier = Modifier,
    size: Dp = 26.dp,
    tint: Color = LaterTerracotta,
    foldTint: Color = LaterDarkAccent,
    cutoutColor: Color = Color(0xFFFCFAF5)
) {
    Canvas(modifier = modifier.size(size)) {
        val w = this.size.width
        val h = this.size.height
        val fold = w * 0.32f

        // 1. Folded Card Silhouette (Top-Right chamfer fold)
        val cardPath = Path().apply {
            moveTo(0f, 0f)
            lineTo(w - fold, 0f)
            lineTo(w, fold)
            lineTo(w, h)
            lineTo(0f, h)
            close()
        }
        drawPath(path = cardPath, color = tint)

        // 2. Crisp Folded Bookmark Flap
        val flapPath = Path().apply {
            moveTo(w - fold, 0f)
            lineTo(w, fold)
            lineTo(w - fold, fold)
            close()
        }
        drawPath(path = flapPath, color = foldTint)

        // 3. Architectural "L" Mark cutout
        val stemLeft = w * 0.26f
        val stemTop = h * 0.28f
        val stemWidth = w * 0.16f
        val baseBottom = h * 0.76f
        val baseRight = w * 0.74f
        val baseHeight = h * 0.16f

        val lPath = Path().apply {
            moveTo(stemLeft, stemTop)
            lineTo(stemLeft + stemWidth, stemTop)
            lineTo(stemLeft + stemWidth, baseBottom - baseHeight)
            lineTo(baseRight, baseBottom - baseHeight)
            lineTo(baseRight, baseBottom)
            lineTo(stemLeft, baseBottom)
            close()
        }

        drawPath(path = lPath, color = cutoutColor)
    }
}

// -----------------------------------------------------------------------------
// VARIANT A: PRIMARY (Terracotta symbol + LATER wordmark on warm paper)
// -----------------------------------------------------------------------------
@Composable
fun LaterPrimaryLogo(
    modifier: Modifier = Modifier,
    markSize: Dp = 24.dp
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        LaterLogoMark(
            size = markSize,
            tint = LaterTerracotta,
            foldTint = LaterDarkAccent,
            cutoutColor = Color(0xFFFCFAF5)
        )

        Spacer(modifier = Modifier.width(10.dp))

        Column {
            Text(
                text = "LATER",
                fontFamily = PlusJakartaSansFamily,
                fontSize = 22.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 2.sp,
                color = LaterInkPrimary
            )

            Text(
                text = "WHY FIRST",
                fontFamily = PlusJakartaSansFamily,
                fontSize = 12.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 1.4.sp,
                color = LaterTerracotta,
                modifier = Modifier.offset(y = (-1).dp)
            )
        }
    }
}

// -----------------------------------------------------------------------------
// VARIANT B: COMPACT (Symbol only)
// -----------------------------------------------------------------------------
@Composable
fun LaterCompactLogo(
    modifier: Modifier = Modifier,
    size: Dp = 28.dp
) {
    LaterLogoMark(
        modifier = modifier,
        size = size,
        tint = LaterTerracotta,
        foldTint = LaterDarkAccent,
        cutoutColor = Color(0xFFFCFAF5)
    )
}

// -----------------------------------------------------------------------------
// VARIANT C: DARK (Ink symbol on warm paper)
// -----------------------------------------------------------------------------
@Composable
fun LaterDarkLogo(
    modifier: Modifier = Modifier,
    size: Dp = 28.dp
) {
    LaterLogoMark(
        modifier = modifier,
        size = size,
        tint = LaterInkPrimary,
        foldTint = Color(0xFF2B2824),
        cutoutColor = LaterPaperBg
    )
}

// -----------------------------------------------------------------------------
// VARIANT D: REVERSED (Cream symbol on dark ink container)
// -----------------------------------------------------------------------------
@Composable
fun LaterReversedLogo(
    modifier: Modifier = Modifier,
    containerSize: Dp = 44.dp,
    markSize: Dp = 24.dp
) {
    Box(
        modifier = modifier
            .size(containerSize)
            .clip(RoundedCornerShape(6.dp))
            .background(LaterInkPrimary),
        contentAlignment = Alignment.Center
    ) {
        LaterLogoMark(
            size = markSize,
            tint = LaterPaperBg,
            foldTint = Color(0xFFDDD7C8),
            cutoutColor = LaterInkPrimary
        )
    }
}
