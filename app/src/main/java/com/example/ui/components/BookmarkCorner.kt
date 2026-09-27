package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.ui.theme.LaterTerracotta

@Composable
fun BookmarkCorner(
    modifier: Modifier = Modifier,
    size: Dp = 14.dp,
    color: Color = LaterTerracotta
) {
    Canvas(modifier = modifier.size(size)) {
        val width = this.size.width
        val height = this.size.height
        val path = Path().apply {
            moveTo(0f, 0f)
            lineTo(width, 0f)
            lineTo(width, height)
            close()
        }
        drawPath(path = path, color = color)
    }
}
