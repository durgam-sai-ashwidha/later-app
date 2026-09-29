package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.LaterBorderSubtle
import com.example.ui.theme.LaterInkPrimary
import com.example.ui.theme.PlusJakartaSansFamily

@Composable
fun CategoryBadge(
    category: String?,
    modifier: Modifier = Modifier
) {
    if (category.isNullOrBlank()) return

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(4.dp))
            .background(LaterBorderSubtle)
            .padding(horizontal = 8.dp, vertical = 3.dp)
    ) {
        Text(
            text = category.uppercase(),
            fontFamily = PlusJakartaSansFamily,
            color = LaterInkPrimary,
            fontSize = 13.sp,
            fontWeight = FontWeight.ExtraBold,
            letterSpacing = 0.8.sp
        )
    }
}
