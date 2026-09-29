package com.example.ui.components

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PlayArrow
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.MemoryEntity
import com.example.ui.theme.LaterBorder
import com.example.ui.theme.LaterBorderSubtle
import com.example.ui.theme.LaterCardBg
import com.example.ui.theme.LaterInkPrimary
import com.example.ui.theme.LaterSecondaryText
import com.example.ui.theme.LaterTerracotta
import com.example.ui.theme.LaterTerracottaLight
import com.example.ui.theme.LaterTextMuted
import com.example.ui.theme.LaterTypographyTokens
import com.example.ui.theme.LaterWhyBg
import com.example.ui.theme.NewsreaderFamily
import com.example.ui.theme.PlusJakartaSansFamily

/**
 * Editorial Memory Card for LATER Archive.
 *
 * Visual & Functional Hierarchy:
 * 1. Top row: Category and time (e.g. "LEARN · 3 HOURS AGO") + folded corner detail + overflow menu
 * 2. Next: Resource title in clear, readable sans-serif
 * 3. Next: Small all-caps label: "WHY YOU SAVED THIS"
 * 4. Next: Saved reason in high-contrast serif quote (Newsreader), limited to 3-4 lines
 * 5. Next: Subtle source domain (e.g. "ishadeed.com" or "product research")
 * 6. Bottom: Specific primary next action with arrow (e.g. "START: REDESIGN DASHBOARD →")
 */
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

    // Tactile micro-interaction on press
    val cardScale by animateFloatAsState(
        targetValue = if (isPressed) 0.985f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioLowBouncy,
            stiffness = Spring.StiffnessMediumLow
        ),
        label = "card_scale"
    )

    val statusLabel = remember(memory) {
        memory.resolveStatus()
    }

    val relativeTime = remember(memory.createdAt) {
        memory.formatRelativeTime()
    }

    val sourceDomain = remember(memory.content) {
        memory.cleanSourceDomain()
    }

    val actionLabel = remember(memory) {
        memory.resolveAction()
    }

    val categoryText = (memory.category ?: "ARCHIVE").uppercase()

    Box(
        modifier = modifier
            .fillMaxWidth()
            .testTag("memory_card_${memory.id}")
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
            .background(LaterCardBg)
            .border(1.dp, LaterBorder, RoundedCornerShape(12.dp))
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                    onClick()
                }
            )
    ) {
        // Small rust-orange folded corner detail in top-right corner
        BookmarkCorner(
            modifier = Modifier.align(Alignment.TopEnd),
            size = 18.dp,
            color = LaterTerracotta
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp, vertical = 18.dp)
        ) {
            // 1. AT THE TOP: Category and status, e.g. “LEARN · NEEDS ACTION” & overflow menu
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .clip(RoundedCornerShape(1.dp))
                            .background(LaterTerracotta)
                    )
                    Text(
                        text = "$categoryText · $statusLabel",
                        style = LaterTypographyTokens.metaCategoryTime
                    )
                }

                // Three-dot overflow menu
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
                            tint = LaterSecondaryText,
                            modifier = Modifier.size(19.dp)
                        )
                    }

                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = { showMenu = false }
                    ) {
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = actionLabel,
                                    fontFamily = PlusJakartaSansFamily,
                                    fontSize = 13.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = LaterTerracotta
                                )
                            },
                            leadingIcon = {
                                Icon(
                                    Icons.Default.PlayArrow,
                                    contentDescription = null,
                                    tint = LaterTerracotta,
                                    modifier = Modifier.size(18.dp)
                                )
                            },
                            onClick = {
                                showMenu = false
                                executeAction(context, memory, actionLabel)
                            }
                        )

                        if (memory.content.startsWith("http://") || memory.content.startsWith("https://")) {
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = "Open Source Link",
                                        fontFamily = PlusJakartaSansFamily,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = LaterInkPrimary
                                    )
                                },
                                leadingIcon = {
                                    Icon(
                                        Icons.AutoMirrored.Filled.OpenInNew,
                                        contentDescription = null,
                                        modifier = Modifier.size(18.dp)
                                    )
                                },
                                onClick = {
                                    showMenu = false
                                    openUrl(context, memory.content)
                                }
                            )
                        }

                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = "Share Memory",
                                    fontFamily = PlusJakartaSansFamily,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = LaterInkPrimary
                                )
                            },
                            leadingIcon = {
                                Icon(
                                    Icons.Default.Share,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp)
                                )
                            },
                            onClick = {
                                showMenu = false
                                shareMemory(context, memory)
                            }
                        )

                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = "Delete",
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

            // 2. NEXT: Clear resource title in readable sans-serif
            Text(
                text = memory.title,
                style = LaterTypographyTokens.titlePrimary,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(14.dp))

            // 3. THEN: Small all-caps label: “WHY YOU SAVED THIS”
            Text(
                text = "WHY YOU SAVED THIS",
                style = LaterTypographyTokens.whyHeaderLabel
            )

            Spacer(modifier = Modifier.height(6.dp))

            // 4. THEN: Saved reason in a beautiful serif quote, limited to 3–4 lines
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(LaterWhyBg.copy(alpha = 0.65f))
                    .padding(horizontal = 14.dp, vertical = 12.dp),
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    text = "“",
                    style = LaterTypographyTokens.whyQuoteMark,
                    modifier = Modifier.padding(end = 4.dp)
                )

                Text(
                    text = memory.why,
                    style = LaterTypographyTokens.whyCard,
                    maxLines = 4,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )
            }

            // 5. SMALL SOURCE ROW: Tiny icon, domain (e.g. “ishadeed.com”), and relative time (e.g. “3h ago”)
            if (sourceDomain.isNotBlank()) {
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .clickable {
                            if (memory.content.startsWith("http")) {
                                openUrl(context, memory.content)
                            }
                        }
                        .padding(vertical = 2.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.OpenInNew,
                        contentDescription = null,
                        tint = LaterTextMuted,
                        modifier = Modifier.size(13.dp)
                    )

                    Text(
                        text = sourceDomain,
                        style = LaterTypographyTokens.sourceBreadcrumb,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Text(
                        text = "·",
                        fontFamily = PlusJakartaSansFamily,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = LaterTextMuted
                    )

                    Text(
                        text = relativeTime,
                        fontFamily = PlusJakartaSansFamily,
                        fontSize = 12.5.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = LaterSecondaryText
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Divider line
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(LaterBorderSubtle)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // 6. AT THE BOTTOM: Strong but minimal bottom action row:
            // “START: REDESIGN DASHBOARD →” or “COMPARE OPTIONS →”
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(7.dp))
                    .background(LaterTerracottaLight)
                    .border(1.dp, LaterTerracotta.copy(alpha = 0.25f), RoundedCornerShape(7.dp))
                    .clickable {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        executeAction(context, memory, actionLabel)
                    }
                    .padding(horizontal = 14.dp, vertical = 11.dp)
                    .testTag("card_action_btn_${memory.id}"),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = actionLabel,
                    style = LaterTypographyTokens.cardAction
                )

                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = "Take action",
                    tint = LaterTerracotta,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}

private fun executeAction(context: Context, memory: MemoryEntity, actionLabel: String) {
    if (memory.content.startsWith("http://") || memory.content.startsWith("https://")) {
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(memory.content)).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
            Toast.makeText(context, "Opening resource for: $actionLabel", Toast.LENGTH_SHORT).show()
            return
        } catch (_: Exception) {}
    }
    Toast.makeText(context, "Action ready: $actionLabel", Toast.LENGTH_SHORT).show()
}

private fun openUrl(context: Context, url: String) {
    try {
        val target = if (!url.startsWith("http://") && !url.startsWith("https://")) "https://$url" else url
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(target)).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)
    } catch (_: Exception) {
        Toast.makeText(context, "Cannot open $url", Toast.LENGTH_SHORT).show()
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

private fun shareMemory(context: Context, memory: MemoryEntity) {
    val shareBody = buildString {
        append("WHY: “${memory.why}”\n\n")
        append("Title: ${memory.title}\n")
        if (memory.content.isNotBlank()) {
            append("Source: ${memory.content}\n")
        }
        append("Action: ${memory.resolveAction()}\n")
        append("\n— Saved with LATER: Save the why. Act when it matters.")
    }

    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_SUBJECT, "LATER: ${memory.title}")
        putExtra(Intent.EXTRA_TEXT, shareBody)
    }
    context.startActivity(Intent.createChooser(intent, "Share memory"))
}
