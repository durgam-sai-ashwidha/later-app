package com.example.ui.components

import android.content.Context
import android.content.Intent
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.example.data.MemoryEntity
import com.example.ui.theme.LaterBorder
import com.example.ui.theme.LaterBorderSubtle
import com.example.ui.theme.LaterCardBg
import com.example.ui.theme.LaterInkPrimary
import com.example.ui.theme.LaterTerracotta
import com.example.ui.theme.LaterWarmGray
import com.example.ui.theme.LaterTextMuted
import com.example.ui.theme.LaterWhyBg
import com.example.ui.theme.LaterTypographyTokens
import com.example.ui.theme.NewsreaderFamily
import com.example.ui.theme.PlusJakartaSansFamily

@Composable
fun MemoryCard(
    memory: MemoryEntity,
    onClick: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current
    var showMenu by remember { mutableStateOf(false) }

    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    // Tactile micro-interaction on card press
    val cardScale by animateFloatAsState(
        targetValue = if (isPressed) 0.985f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioLowBouncy,
            stiffness = Spring.StiffnessMediumLow
        ),
        label = "card_scale"
    )

    // Subtle responsive offset on the bookmark corner motif
    val cornerOffset by animateDpAsState(
        targetValue = if (isPressed) 1.dp else 0.dp,
        label = "corner_offset"
    )

    val relativeTime = remember(memory.createdAt) {
        formatArchiveRelativeTime(memory.createdAt)
    }

    val cleanSource = remember(memory.content) {
        memory.content
            .removePrefix("http://")
            .removePrefix("https://")
            .removePrefix("www.")
    }

    // Archival index card: restrained 6dp corner, subtle hairline border, minimal paper shadow
    Box(
        modifier = modifier
            .fillMaxWidth()
            .testTag("memory_card_${memory.id}")
            .graphicsLayer {
                scaleX = cardScale
                scaleY = cardScale
            }
            .shadow(
                elevation = 1.dp,
                shape = RoundedCornerShape(6.dp),
                ambientColor = LaterInkPrimary.copy(alpha = 0.03f),
                spotColor = LaterInkPrimary.copy(alpha = 0.05f)
            )
            .clip(RoundedCornerShape(6.dp))
            .background(LaterCardBg)
            .border(1.dp, LaterBorder, RoundedCornerShape(6.dp))
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                    onClick()
                }
            )
    ) {
        // Signature Creative Motif: Archival Corner Ribbon with subtle tactile reaction
        BookmarkCorner(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .offset(x = cornerOffset, y = -cornerOffset),
            size = 14.dp,
            color = LaterTerracotta
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 22.dp, vertical = 20.dp)
        ) {
            // Top Row: "WHY YOU SAVED THIS" label & options menu
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "WHY YOU SAVED THIS",
                    style = LaterTypographyTokens.whyHeaderLabel
                )

                Box {
                    IconButton(
                        onClick = { showMenu = true },
                        modifier = Modifier
                            .size(28.dp)
                            .testTag("card_menu_button_${memory.id}")
                    ) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "Options",
                            tint = LaterWarmGray,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = { showMenu = false }
                    ) {
                        DropdownMenuItem(
                            text = {
                                Text(
                                    "Share",
                                    fontFamily = PlusJakartaSansFamily,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = LaterInkPrimary
                                )
                            },
                            leadingIcon = {
                                Icon(Icons.Default.Share, contentDescription = null, modifier = Modifier.size(18.dp))
                            },
                            onClick = {
                                showMenu = false
                                shareMemory(context, memory)
                            }
                        )
                        DropdownMenuItem(
                            text = {
                                Text(
                                    "Delete",
                                    fontFamily = PlusJakartaSansFamily,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = LaterTerracotta
                                )
                            },
                            leadingIcon = {
                                Icon(
                                    Icons.Default.DeleteOutline,
                                    contentDescription = null,
                                    tint = LaterTerracotta,
                                    modifier = Modifier.size(18.dp)
                                )
                            },
                            onClick = {
                                showMenu = false
                                onDelete()
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // 1. Dominant WHY: Heart of LATER (strictly enforced editorial serif token)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(6.dp))
                    .background(LaterWhyBg.copy(alpha = 0.6f))
                    .padding(horizontal = 16.dp, vertical = 14.dp),
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    text = "“",
                    style = LaterTypographyTokens.whyQuoteMark,
                    modifier = Modifier.padding(end = 6.dp)
                )

                Text(
                    text = memory.why,
                    style = LaterTypographyTokens.whyCard,
                    maxLines = 5,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // 2. TITLE: Clear, readable, secondary sans-serif token
            Text(
                text = memory.title,
                style = LaterTypographyTokens.titlePrimary,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(8.dp))

            // 3. CATEGORY / TIME (e.g. "LEARN · 3H AGO")
            val categoryLabel = (memory.category ?: "ARCHIVE").uppercase()
            Text(
                text = "$categoryLabel · ${relativeTime.uppercase()}",
                style = LaterTypographyTokens.metaCategoryTime
            )

            // 4. SOURCE: Quiet breadcrumb
            if (cleanSource.isNotBlank()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = cleanSource,
                    style = LaterTypographyTokens.sourceBreadcrumb,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

fun formatArchiveRelativeTime(timestamp: Long): String {
    val now = System.currentTimeMillis()
    val diff = (now - timestamp).coerceAtLeast(0)
    val seconds = diff / 1000
    val minutes = seconds / 60
    val hours = minutes / 60
    val days = hours / 24
    val weeks = days / 7
    val months = days / 30
    val years = days / 365

    return when {
        seconds < 45 -> "just now"
        minutes == 1L -> "1 minute ago"
        minutes < 60 -> "$minutes minutes ago"
        hours == 1L -> "1 hour ago"
        hours < 24 -> "$hours hours ago"
        days == 1L -> "yesterday"
        days < 7 -> "$days days ago"
        weeks == 1L -> "1 week ago"
        weeks < 5 -> "$weeks weeks ago"
        months == 1L -> "1 month ago"
        months < 12 -> "$months months ago"
        years == 1L -> "1 year ago"
        else -> "$years years ago"
    }
}

fun formatRelativeTime(timestamp: Long): String {
    return formatArchiveRelativeTime(timestamp)
}

private fun shareMemory(context: Context, memory: MemoryEntity) {
    val shareBody = buildString {
        append("WHY: ${memory.why}\n\n")
        append("Title: ${memory.title}\n")
        if (memory.content.isNotBlank()) {
            append("Source: ${memory.content}\n")
        }
        append("\nSaved with LATER")
    }

    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_SUBJECT, "LATER: ${memory.title}")
        putExtra(Intent.EXTRA_TEXT, shareBody)
    }
    context.startActivity(Intent.createChooser(intent, "Share memory"))
}
