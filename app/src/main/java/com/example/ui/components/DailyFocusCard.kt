package com.example.ui.components

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.LaterBorder
import com.example.ui.theme.LaterInkPrimary
import com.example.ui.theme.LaterSecondaryText
import com.example.ui.theme.LaterTerracotta
import com.example.ui.theme.LaterTerracottaLight
import com.example.ui.theme.NewsreaderFamily
import com.example.ui.theme.PlusJakartaSansFamily

import com.example.data.MemoryEntity

/**
 * 3. Featured Daily-Focus Card for LATER
 * Dynamically binds to a real memory or demo workspace memory.
 */
@Composable
fun DailyFocusCard(
    memory: MemoryEntity,
    modifier: Modifier = Modifier,
    onStartFocusSession: () -> Unit
) {
    val haptic = LocalHapticFeedback.current
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val cardScale by animateFloatAsState(
        targetValue = if (isPressed) 0.985f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioLowBouncy,
            stiffness = Spring.StiffnessMediumLow
        ),
        label = "focus_card_scale"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .testTag("daily_focus_card")
            .graphicsLayer {
                scaleX = cardScale
                scaleY = cardScale
            }
            .shadow(
                elevation = 2.dp,
                shape = RoundedCornerShape(12.dp),
                ambientColor = LaterInkPrimary.copy(alpha = 0.05f),
                spotColor = LaterInkPrimary.copy(alpha = 0.06f)
            )
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFFFAF7F0)) // Light warm-tinted background
            .border(1.dp, LaterBorder, RoundedCornerShape(12.dp))
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                    onStartFocusSession()
                }
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
        ) {
            // Orange accent line on the left edge
            Box(
                modifier = Modifier
                    .width(4.5.dp)
                    .fillMaxHeight()
                    .background(LaterTerracotta)
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp, vertical = 14.dp)
            ) {
                // Header row: Label + Compact metadata
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Small uppercase label: “TODAY’S FOCUS”
                    Text(
                        text = "TODAY’S FOCUS",
                        fontFamily = PlusJakartaSansFamily,
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 1.2.sp,
                        color = LaterTerracotta
                    )

                    // Compact metadata: e.g. “20 MIN · LEARN”
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(LaterTerracottaLight)
                            .padding(horizontal = 7.dp, vertical = 3.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Timer,
                            contentDescription = null,
                            tint = LaterTerracotta,
                            modifier = Modifier.size(12.dp)
                        )
                        Text(
                            text = "${memory.estimatedMinutes} MIN · ${(memory.category ?: "FOCUS").uppercase()}",
                            fontFamily = PlusJakartaSansFamily,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.7.sp,
                            color = LaterTerracotta
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Title from memory
                Text(
                    text = memory.title,
                    fontFamily = PlusJakartaSansFamily,
                    fontSize = 17.5.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 22.sp,
                    color = LaterInkPrimary
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Supporting text: saved why
                Text(
                    text = memory.why,
                    fontFamily = NewsreaderFamily,
                    fontSize = 14.5.sp,
                    fontWeight = FontWeight.Medium,
                    lineHeight = 20.sp,
                    color = LaterSecondaryText
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Clear rust-orange text action: “START FOCUS SESSION →”
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier
                        .testTag("start_focus_session_btn")
                        .clip(RoundedCornerShape(6.dp))
                        .clickable {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            onStartFocusSession()
                        }
                        .padding(vertical = 2.dp)
                ) {
                    Text(
                        text = "START FOCUS SESSION",
                        fontFamily = PlusJakartaSansFamily,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 0.9.sp,
                        color = LaterTerracotta
                    )

                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = "Start Focus Session",
                        tint = LaterTerracotta,
                        modifier = Modifier.size(15.dp)
                    )
                }
            }
        }
    }
}
