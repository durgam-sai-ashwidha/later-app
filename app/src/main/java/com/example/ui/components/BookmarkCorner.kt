package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.ui.theme.LaterDarkAccent
import com.example.ui.theme.LaterTerracotta

/**
 * Editorial Folded Corner Detail for LATER memory cards.
 * Sits in the top-right corner, evocative of a page turn / dog-eared reading journal.
 */
@Composable
fun BookmarkCorner(
    modifier: Modifier = Modifier,
    size: Dp = 16.dp,
    color: Color = LaterTerracotta,
    foldColor: Color = LaterDarkAccent
) {
    Canvas(modifier = modifier.size(size)) {
        val w = this.size.width
        val h = this.size.height

        // Right triangle corner
        val path = Path().apply {
            moveTo(0f, 0f)
            lineTo(w, 0f)
            lineTo(w, h)
            close()
        }
        drawPath(path = path, color = color)

        // Subtle fold diagonal line
        drawLine(
            color = foldColor.copy(alpha = 0.5f),
            start = Offset(0f, 0f),
            end = Offset(w, h),
            strokeWidth = 1.5f
        )
    }
}
