package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.MemoryEntity
import com.example.ui.components.BookmarkCorner
import com.example.ui.components.LaterLogoMark
import com.example.ui.theme.LaterBorder
import com.example.ui.theme.LaterBorderSubtle
import com.example.ui.theme.LaterCardBg
import com.example.ui.theme.LaterDarkAccent
import com.example.ui.theme.LaterInkPrimary
import com.example.ui.theme.LaterPaperBg
import com.example.ui.theme.LaterSecondaryText
import com.example.ui.theme.LaterTerracotta
import com.example.ui.theme.LaterTerracottaLight
import com.example.ui.theme.LaterTextMuted
import com.example.ui.theme.LaterTypographyTokens
import com.example.ui.theme.LaterWhyBg
import com.example.ui.theme.NewsreaderFamily
import com.example.ui.theme.PlusJakartaSansFamily
import com.example.viewmodel.MemoryViewModel

/**
 * Subtle rust-orange line illustration (52 px) representing a saved memory
 * returning at the right time. Clean architectural vector lines without
 * generic AI sparkles or gradient clutter.
 */
@Composable
fun SavedMemoryReturnIllustration(
    modifier: Modifier = Modifier,
    tint: Color = LaterTerracotta
) {
    Canvas(
        modifier = modifier
            .size(52.dp)
            .testTag("now_empty_memory_illustration")
    ) {
        val w = size.width
        val h = size.height

        val strokeWidth = 1.75.dp.toPx()
        val hairline = 1.1.dp.toPx()

        // 1. Subtle circular time/return orbit arc (representing time alignment)
        drawArc(
            color = tint.copy(alpha = 0.35f),
            startAngle = 100f,
            sweepAngle = 265f,
            useCenter = false,
            topLeft = Offset(w * 0.08f, h * 0.08f),
            size = Size(w * 0.84f, h * 0.84f),
            style = Stroke(width = hairline, cap = StrokeCap.Round)
        )

        // Time dial markers at 12 o'clock and 3 o'clock
        drawLine(
            color = tint.copy(alpha = 0.5f),
            start = Offset(w * 0.5f, h * 0.03f),
            end = Offset(w * 0.5f, h * 0.11f),
            strokeWidth = 1.4.dp.toPx(),
            cap = StrokeCap.Round
        )
        drawLine(
            color = tint.copy(alpha = 0.5f),
            start = Offset(w * 0.97f, h * 0.5f),
            end = Offset(w * 0.89f, h * 0.5f),
            strokeWidth = 1.4.dp.toPx(),
            cap = StrokeCap.Round
        )

        // 2. Saved memory document card in center
        val cardLeft = w * 0.28f
        val cardTop = h * 0.20f
        val cardWidth = w * 0.44f
        val cardHeight = h * 0.55f

        // Card rectangle with rounded corners
        drawRoundRect(
            color = tint,
            topLeft = Offset(cardLeft, cardTop),
            size = Size(cardWidth, cardHeight),
            cornerRadius = CornerRadius(4.dp.toPx()),
            style = Stroke(width = strokeWidth, cap = StrokeCap.Round, join = StrokeJoin.Round)
        )

        // Memory Bookmark ribbon fold on top right
        val ribbonLeft = cardLeft + cardWidth - w * 0.12f
        val ribbonPath = Path().apply {
            moveTo(ribbonLeft, cardTop)
            lineTo(ribbonLeft, cardTop + h * 0.18f)
            lineTo(ribbonLeft + w * 0.06f, cardTop + h * 0.13f)
            lineTo(ribbonLeft + w * 0.12f, cardTop + h * 0.18f)
            lineTo(ribbonLeft + w * 0.12f, cardTop)
        }
        drawPath(
            path = ribbonPath,
            color = tint,
            style = Stroke(width = strokeWidth, cap = StrokeCap.Round, join = StrokeJoin.Round)
        )

        // Saved "Why" line notes inside card
        drawLine(
            color = tint,
            start = Offset(cardLeft + w * 0.07f, cardTop + h * 0.28f),
            end = Offset(cardLeft + cardWidth - w * 0.16f, cardTop + h * 0.28f),
            strokeWidth = 1.5.dp.toPx(),
            cap = StrokeCap.Round
        )
        drawLine(
            color = tint.copy(alpha = 0.65f),
            start = Offset(cardLeft + w * 0.07f, cardTop + h * 0.38f),
            end = Offset(cardLeft + cardWidth - w * 0.20f, cardTop + h * 0.38f),
            strokeWidth = 1.5.dp.toPx(),
            cap = StrokeCap.Round
        )

        // 3. Returning swoop path from orbit entering back into current moment
        val swoop = Path().apply {
            moveTo(w * 0.14f, h * 0.74f)
            cubicTo(
                w * 0.12f, h * 0.90f,
                w * 0.32f, h * 0.93f,
                cardLeft + w * 0.12f, cardTop + cardHeight + 1.dp.toPx()
            )
        }
        drawPath(
            path = swoop,
            color = tint,
            style = Stroke(width = strokeWidth, cap = StrokeCap.Round, join = StrokeJoin.Round)
        )

        // Clean arrowhead pointing into card
        val tip = Offset(cardLeft + w * 0.12f, cardTop + cardHeight + 1.dp.toPx())
        drawLine(
            color = tint,
            start = Offset(tip.x - 3.5.dp.toPx(), tip.y + 4.dp.toPx()),
            end = tip,
            strokeWidth = strokeWidth,
            cap = StrokeCap.Round
        )
        drawLine(
            color = tint,
            start = Offset(tip.x + 3.5.dp.toPx(), tip.y + 3.5.dp.toPx()),
            end = tip,
            strokeWidth = strokeWidth,
            cap = StrokeCap.Round
        )
    }
}

@Composable
fun TodayScreen(
    viewModel: MemoryViewModel,
    onStartFocusSession: (MemoryEntity?) -> Unit,
    onNavigateToDetail: (String) -> Unit,
    onOpenProfile: () -> Unit,
    onSaveTheWhy: () -> Unit = {}
) {
    val haptic = LocalHapticFeedback.current
    val todayFocus by viewModel.todayFocusMemory.collectAsStateWithLifecycle()
    val worthRevisiting by viewModel.worthRevisitingMemories.collectAsStateWithLifecycle()
    val comingUp by viewModel.comingUpMemories.collectAsStateWithLifecycle()
    val focusedActionsToday by viewModel.focusedActionsToday.collectAsStateWithLifecycle()
    val streak by viewModel.followThroughStreak.collectAsStateWithLifecycle()
    val heroDismissed by viewModel.heroDismissedToday.collectAsStateWithLifecycle()
    val isDemoMode by viewModel.isDemoMode.collectAsStateWithLifecycle()

    val isFreshEmpty = todayFocus == null && worthRevisiting.isEmpty() && comingUp.isEmpty() && !heroDismissed

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(LaterPaperBg)
            .testTag("now_screen")
    ) {
        // =====================================================================
        // HEADER: Reduced LATER logo + NOW identity + Profile icon
        // =====================================================================
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, end = 20.dp, top = 12.dp, bottom = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clickable {
                        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                        onOpenProfile()
                    }
                    .testTag("now_brand_header")
            ) {
                // Reduced LATER logo slightly (22.dp)
                LaterLogoMark(
                    size = 22.dp,
                    tint = LaterTerracotta,
                    foldTint = LaterDarkAccent,
                    cutoutColor = LaterPaperBg
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = "LATER",
                    fontFamily = PlusJakartaSansFamily,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 1.2.sp,
                    color = LaterInkPrimary
                )

                Spacer(modifier = Modifier.width(8.dp))

                // Small rust-orange Now identity: “NOW • ONE THING THAT MATTERS”
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(LaterTerracottaLight)
                        .border(1.dp, LaterTerracotta.copy(alpha = 0.25f), RoundedCornerShape(4.dp))
                        .padding(horizontal = 7.dp, vertical = 2.5.dp)
                        .testTag("now_status_pill")
                ) {
                    Text(
                        text = "NOW • ONE THING THAT MATTERS",
                        fontFamily = PlusJakartaSansFamily,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 0.7.sp,
                        color = LaterTerracotta
                    )
                }
            }

            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(LaterCardBg)
                    .border(1.dp, LaterBorder, CircleShape)
                    .clickable {
                        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                        onOpenProfile()
                    }
                    .testTag("now_profile_button"),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.Person,
                    contentDescription = "Profile and settings",
                    tint = LaterInkPrimary,
                    modifier = Modifier.size(17.dp)
                )
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 2.dp, bottom = 96.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Main Greeting & Question:
            // Greeting small and quiet: “Good morning,”
            // Primary headline larger, stronger, and split across two lines:
            // “What deserves your
            //  attention today?”
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 2.dp, bottom = 4.dp)
                ) {
                    Text(
                        text = "Good morning,",
                        fontFamily = PlusJakartaSansFamily,
                        fontSize = 13.5.sp,
                        fontWeight = FontWeight.Normal,
                        letterSpacing = 0.2.sp,
                        color = LaterSecondaryText
                    )

                    Spacer(modifier = Modifier.height(3.dp))

                    Text(
                        text = "What deserves your\nattention today?",
                        fontFamily = NewsreaderFamily,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.SemiBold,
                        lineHeight = 36.sp,
                        letterSpacing = (-0.5).sp,
                        color = LaterInkPrimary
                    )

                    // Daily completion indicator: shown only when there is actual progress or streak
                    if (focusedActionsToday > 0 || streak > 0) {
                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .background(LaterTerracottaLight)
                                .border(1.dp, LaterTerracotta.copy(alpha = 0.25f), RoundedCornerShape(20.dp))
                                .padding(horizontal = 12.dp, vertical = 5.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(15.dp)
                                    .clip(CircleShape)
                                    .background(LaterTerracotta),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(10.dp)
                                )
                            }
                            Text(
                                text = "$focusedActionsToday focused action today",
                                fontFamily = PlusJakartaSansFamily,
                                fontSize = 11.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = LaterTerracotta
                            )
                            if (streak > 0) {
                                Text(
                                    text = "·",
                                    fontFamily = PlusJakartaSansFamily,
                                    fontSize = 11.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = LaterTerracotta.copy(alpha = 0.5f)
                                )
                                Text(
                                    text = "$streak-day follow-through 🔥",
                                    fontFamily = PlusJakartaSansFamily,
                                    fontSize = 11.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = LaterTerracotta
                                )
                            }
                        }
                    }
                }
            }

            if (isFreshEmpty) {
                // Now screen fresh empty state
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(LaterCardBg)
                            .border(1.dp, LaterBorderSubtle, RoundedCornerShape(16.dp))
                            .padding(horizontal = 24.dp, vertical = 34.dp)
                            .testTag("now_empty_state"),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            // Subtle rust-orange line illustration representing a saved memory returning at the right time
                            SavedMemoryReturnIllustration(
                                modifier = Modifier.size(52.dp),
                                tint = LaterTerracotta
                            )

                            Spacer(modifier = Modifier.height(18.dp))

                            Text(
                                text = "Nothing needs your attention yet.",
                                fontFamily = NewsreaderFamily,
                                fontSize = 23.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = LaterInkPrimary,
                                textAlign = TextAlign.Center
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = "Save something with a reason,\nand LATER will bring it back\nwhen it matters.",
                                fontFamily = PlusJakartaSansFamily,
                                fontSize = 14.sp,
                                color = LaterSecondaryText,
                                lineHeight = 21.sp,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(horizontal = 8.dp)
                            )

                            Spacer(modifier = Modifier.height(22.dp))

                            Button(
                                onClick = {
                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                    onSaveTheWhy()
                                },
                                modifier = Modifier
                                    .height(48.dp)
                                    .testTag("now_save_the_why_button"),
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = LaterTerracotta,
                                    contentColor = Color.White
                                )
                            ) {
                                Text(
                                    text = "SAVE THE WHY",
                                    fontFamily = PlusJakartaSansFamily,
                                    fontSize = 12.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 0.8.sp
                                )
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            Text(
                                text = "Your attention, intentionally.",
                                fontFamily = PlusJakartaSansFamily,
                                fontSize = 11.5.sp,
                                fontWeight = FontWeight.Medium,
                                letterSpacing = 0.4.sp,
                                color = LaterTextMuted,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            } else {

            // =================================================================
            // 2. A SINGLE LARGE HERO CARD, NEVER MORE THAN ONE:
            // - Label: “LATER MOMENT”
            // - Trigger: “Your dashboard sprint starts today”
            // - Item: “Modern CSS Grid & Subgrid Deep Dive”
            // - Saved why in serif: “Need this for next sprint’s dashboard redesign—subgrid may solve the card-alignment issue.”
            // - Tiny action: “20-MIN LAYOUT RESEARCH”
            // - Primary button: “START FOLLOW-THROUGH →”
            // - Secondary action: “NOT TODAY”
            // =================================================================
            item {
                if (heroDismissed || todayFocus == null) {
                    // Calm placeholder if dismissed for today
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(LaterCardBg)
                            .border(1.dp, LaterBorderSubtle, RoundedCornerShape(14.dp))
                            .padding(18.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "All caught up for now.",
                                    fontFamily = PlusJakartaSansFamily,
                                    fontSize = 14.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = LaterInkPrimary
                                )
                                Text(
                                    text = "You postponed today's primary moment. Revisit anytime below.",
                                    fontFamily = PlusJakartaSansFamily,
                                    fontSize = 12.5.sp,
                                    color = LaterSecondaryText
                                )
                            }
                            Text(
                                text = "Undo",
                                fontFamily = PlusJakartaSansFamily,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = LaterTerracotta,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .clickable { viewModel.restoreHeroToday() }
                                    .padding(horizontal = 8.dp, vertical = 6.dp)
                            )
                        }
                    }
                } else {
                    val focusMemory = todayFocus!!
                    val heroTrigger = focusMemory.resolveTrigger()
                    val heroItem = focusMemory.title
                    val heroWhy = focusMemory.why
                    val heroTinyAction = focusMemory.resolveTinyAction().uppercase()

                    val interactionSource = remember { MutableInteractionSource() }
                    val isPressed by interactionSource.collectIsPressedAsState()
                    val cardScale by animateFloatAsState(
                        targetValue = if (isPressed) 0.985f else 1f,
                        animationSpec = spring(dampingRatio = Spring.DampingRatioLowBouncy),
                        label = "hero_card_scale"
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .graphicsLayer {
                                scaleX = cardScale
                                scaleY = cardScale
                            }
                            .shadow(
                                elevation = 4.dp,
                                shape = RoundedCornerShape(14.dp),
                                ambientColor = LaterInkPrimary.copy(alpha = 0.07f),
                                spotColor = LaterInkPrimary.copy(alpha = 0.09f)
                            )
                            .clip(RoundedCornerShape(14.dp))
                            .background(Color(0xFFFAF7F0)) // Warm tinted background
                            .border(1.dp, LaterBorder, RoundedCornerShape(14.dp))
                            .testTag("now_hero_card")
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(IntrinsicSize.Min)
                        ) {
                            // Orange accent line on left edge
                            Box(
                                modifier = Modifier
                                    .width(6.dp)
                                    .fillMaxHeight()
                                    .background(LaterTerracotta)
                            )

                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(18.dp)
                            ) {
                                // Label: “LATER MOMENT”
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Box(
                                            modifier = Modifier
                                                .size(7.dp)
                                                .clip(RoundedCornerShape(1.dp))
                                                .background(LaterTerracotta)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = "LATER MOMENT",
                                            fontFamily = PlusJakartaSansFamily,
                                            fontSize = 11.5.sp,
                                            fontWeight = FontWeight.ExtraBold,
                                            letterSpacing = 1.3.sp,
                                            color = LaterTerracotta
                                        )
                                    }

                                    // Compact 20 MIN indicator
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(4.dp))
                                            .background(LaterTerracottaLight)
                                            .padding(horizontal = 8.dp, vertical = 3.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Outlined.Timer,
                                            contentDescription = null,
                                            tint = LaterTerracotta,
                                            modifier = Modifier.size(12.dp)
                                        )
                                        Text(
                                            text = "${focusMemory.estimatedMinutes} MIN",
                                            fontFamily = PlusJakartaSansFamily,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = LaterTerracotta
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(10.dp))

                                // Trigger: “Your dashboard sprint starts today”
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = "TRIGGER: ",
                                        fontFamily = PlusJakartaSansFamily,
                                        fontSize = 10.5.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        letterSpacing = 0.8.sp,
                                        color = LaterTextMuted
                                    )
                                    Text(
                                        text = heroTrigger,
                                        fontFamily = PlusJakartaSansFamily,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = LaterInkPrimary
                                    )
                                }

                                Spacer(modifier = Modifier.height(6.dp))

                                // Item: “Modern CSS Grid & Subgrid Deep Dive”
                                Text(
                                    text = heroItem,
                                    fontFamily = PlusJakartaSansFamily,
                                    fontSize = 18.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    lineHeight = 23.sp,
                                    color = LaterInkPrimary
                                )

                                Spacer(modifier = Modifier.height(10.dp))

                                // Saved why in serif: “Need this for next sprint’s dashboard redesign—subgrid may solve the card-alignment issue.”
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(LaterWhyBg.copy(alpha = 0.75f))
                                        .border(1.dp, LaterBorderSubtle, RoundedCornerShape(8.dp))
                                        .padding(horizontal = 14.dp, vertical = 10.dp)
                                ) {
                                    Text(
                                        text = "“$heroWhy”",
                                        fontFamily = NewsreaderFamily,
                                        fontSize = 14.5.sp,
                                        lineHeight = 21.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = LaterInkPrimary
                                    )
                                }

                                Spacer(modifier = Modifier.height(12.dp))

                                // Tiny action label: “20-MIN LAYOUT RESEARCH”
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = "TINY ACTION: ",
                                        fontFamily = PlusJakartaSansFamily,
                                        fontSize = 10.5.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        letterSpacing = 0.8.sp,
                                        color = LaterTextMuted
                                    )
                                    Text(
                                        text = heroTinyAction,
                                        fontFamily = PlusJakartaSansFamily,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        letterSpacing = 0.6.sp,
                                        color = LaterTerracotta
                                    )
                                }

                                Spacer(modifier = Modifier.height(16.dp))

                                // Action Row: Primary button “START FOLLOW-THROUGH →” & Secondary “NOT TODAY”
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(LaterTerracotta)
                                            .clickable {
                                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                                onStartFocusSession(focusMemory)
                                            }
                                            .padding(horizontal = 14.dp, vertical = 11.dp)
                                            .testTag("now_start_follow_through_btn")
                                    ) {
                                        Text(
                                            text = "START FOLLOW-THROUGH",
                                            fontFamily = PlusJakartaSansFamily,
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.ExtraBold,
                                            letterSpacing = 0.8.sp,
                                            color = Color.White
                                        )
                                        Icon(
                                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                            contentDescription = null,
                                            tint = Color.White,
                                            modifier = Modifier.size(15.dp)
                                        )
                                    }

                                    Text(
                                        text = "NOT TODAY",
                                        fontFamily = PlusJakartaSansFamily,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        letterSpacing = 0.8.sp,
                                        color = LaterTextMuted,
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(6.dp))
                                            .clickable {
                                                haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                                viewModel.dismissHeroToday()
                                            }
                                            .padding(horizontal = 10.dp, vertical = 8.dp)
                                            .testTag("now_not_today_btn")
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // =================================================================
            // COMPACT SECTION 1: “WORTH REVISITING”
            // Calm, non-crowded list of 2 smart memory cards
            // =================================================================
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "WORTH REVISITING",
                        fontFamily = PlusJakartaSansFamily,
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 1.1.sp,
                        color = LaterInkPrimary
                    )
                    Text(
                        text = "${worthRevisiting.size} items",
                        fontFamily = PlusJakartaSansFamily,
                        fontSize = 11.5.sp,
                        color = LaterTextMuted
                    )
                }
            }

            items(worthRevisiting) { memory ->
                val cardInteractionSource = remember { MutableInteractionSource() }
                val isCardPressed by cardInteractionSource.collectIsPressedAsState()
                val scale by animateFloatAsState(
                    targetValue = if (isCardPressed) 0.985f else 1f,
                    label = "worth_revisiting_scale"
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .graphicsLayer {
                            scaleX = scale
                            scaleY = scale
                        }
                        .shadow(1.5.dp, RoundedCornerShape(10.dp), ambientColor = LaterInkPrimary.copy(alpha = 0.04f))
                        .clip(RoundedCornerShape(10.dp))
                        .background(LaterCardBg)
                        .border(1.dp, LaterBorderSubtle, RoundedCornerShape(10.dp))
                        .clickable(
                            interactionSource = cardInteractionSource,
                            indication = null,
                            onClick = { onNavigateToDetail(memory.id) }
                        )
                        .padding(14.dp)
                        .testTag("worth_revisiting_card_${memory.id}")
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "${(memory.category ?: "ITEM").uppercase()} · ${memory.resolveStatus()}",
                                style = LaterTypographyTokens.metaCategoryTime
                            )
                            Text(
                                text = memory.relevanceLabel ?: "CONTEXT MATCH",
                                fontFamily = PlusJakartaSansFamily,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = LaterTerracotta
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = memory.title,
                            fontFamily = PlusJakartaSansFamily,
                            fontSize = 14.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = LaterInkPrimary,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = "“${memory.why}”",
                            fontFamily = NewsreaderFamily,
                            fontSize = 13.5.sp,
                            lineHeight = 18.sp,
                            color = LaterSecondaryText,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        // Quick action button
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(6.dp))
                                .background(LaterTerracottaLight)
                                .clickable {
                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                    onStartFocusSession(memory)
                                }
                                .padding(horizontal = 10.dp, vertical = 7.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = memory.resolveAction(),
                                fontFamily = PlusJakartaSansFamily,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = LaterTerracotta
                            )
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = null,
                                tint = LaterTerracotta,
                                modifier = Modifier.size(13.dp)
                            )
                        }
                    }
                }
            }

            // =================================================================
            // COMPACT SECTION 2: “COMING UP”
            // Upcoming moments and intent triggers
            // =================================================================
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "COMING UP",
                        fontFamily = PlusJakartaSansFamily,
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 1.1.sp,
                        color = LaterInkPrimary
                    )
                    Text(
                        text = "Intent triggers",
                        fontFamily = PlusJakartaSansFamily,
                        fontSize = 11.5.sp,
                        color = LaterTextMuted
                    )
                }
            }

            items(comingUp) { item ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(LaterCardBg)
                        .border(1.dp, LaterBorderSubtle, RoundedCornerShape(10.dp))
                        .clickable { onNavigateToDetail(item.id) }
                        .padding(14.dp)
                        .testTag("coming_up_card_${item.id}")
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .background(LaterTerracottaLight),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Schedule,
                                    contentDescription = null,
                                    tint = LaterTerracotta,
                                    modifier = Modifier.size(16.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(10.dp))

                            Column {
                                Text(
                                    text = item.resolveTrigger(),
                                    fontFamily = PlusJakartaSansFamily,
                                    fontSize = 11.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = LaterTerracotta
                                )
                                Text(
                                    text = item.title,
                                    fontFamily = PlusJakartaSansFamily,
                                    fontSize = 13.5.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = LaterInkPrimary,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }

                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = "View item",
                            tint = LaterTextMuted,
                            modifier = Modifier.size(15.dp)
                        )
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(40.dp))
            }
            }
        }
    }
}
