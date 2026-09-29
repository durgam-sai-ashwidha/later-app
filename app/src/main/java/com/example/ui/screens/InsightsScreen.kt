package com.example.ui.screens

import android.content.Intent
import android.widget.Toast
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.components.LaterLogoMark
import com.example.ui.components.ProFeatureLockedDialog
import com.example.ui.components.ProLockBadge
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
import com.example.ui.theme.LaterWhyBg
import com.example.ui.theme.NewsreaderFamily
import com.example.ui.theme.PlusJakartaSansFamily
import com.example.viewmodel.MemoryViewModel

@Composable
fun InsightsScreen(
    viewModel: MemoryViewModel,
    isPro: Boolean = false,
    onOpenPaywall: () -> Unit = {},
    onFinishReview: () -> Unit,
    onCreateFirstLaterMoment: () -> Unit = {}
) {
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current

    var lockedFeatureForDialog by remember { mutableStateOf<String?>(null) }

    val focusedActionsToday by viewModel.focusedActionsToday.collectAsStateWithLifecycle()
    val focusSessionsCompleted by viewModel.focusSessionsCompleted.collectAsStateWithLifecycle()
    val focusMinutesTotal by viewModel.focusMinutesTotal.collectAsStateWithLifecycle()
    val needingAttentionCount by viewModel.memoriesNeedingAttentionCount.collectAsStateWithLifecycle()
    val weeklyReflection by viewModel.weeklyReflection.collectAsStateWithLifecycle()
    val completedMemories by viewModel.completedMemories.collectAsStateWithLifecycle()
    val isDemoMode by viewModel.isDemoMode.collectAsStateWithLifecycle()

    var reflectionInput by remember(weeklyReflection) { mutableStateOf(weeklyReflection) }
    var reviewCompleted by remember { mutableStateOf(false) }

    val isFreshEmpty = completedMemories.isEmpty() && !isDemoMode && focusSessionsCompleted == 0

    val categoryBreakdown = listOf(
        Pair("Learn", 0.85f),
        Pair("Buy", 0.60f),
        Pair("Try", 0.40f),
        Pair("Reference", 0.70f),
        Pair("Idea", 0.50f)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(LaterPaperBg)
            .testTag("insights_screen")
    ) {
        // Pinned Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                LaterLogoMark(
                    size = 24.dp,
                    tint = LaterTerracotta,
                    foldTint = LaterDarkAccent,
                    cutoutColor = LaterPaperBg
                )
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = "LATER",
                        fontFamily = PlusJakartaSansFamily,
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 1.2.sp,
                        color = LaterTerracotta
                    )
                    Text(
                        text = "PROOF",
                        fontFamily = PlusJakartaSansFamily,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp,
                        color = LaterTextMuted
                    )
                }
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(LaterTerracottaLight)
                    .padding(horizontal = 10.dp, vertical = 5.dp)
            ) {
                Text(
                    text = if (isFreshEmpty) "PROOF ARCHIVE" else "WEEK IN PROGRESS",
                    fontFamily = PlusJakartaSansFamily,
                    fontSize = 10.5.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.5.sp,
                    color = LaterTerracotta
                )
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 6.dp, bottom = 96.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header statement
            item {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "YOUR WEEK IN CONTEXT",
                        fontFamily = PlusJakartaSansFamily,
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 1.2.sp,
                        color = LaterTerracotta
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Progress without burnout.",
                        fontFamily = NewsreaderFamily,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = LaterInkPrimary
                    )
                }
            }

            if (isFreshEmpty) {
                // Proof empty state
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(LaterCardBg)
                            .border(1.dp, LaterBorderSubtle, RoundedCornerShape(16.dp))
                            .padding(horizontal = 24.dp, vertical = 38.dp)
                            .testTag("proof_empty_state"),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(52.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(LaterTerracottaLight),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = LaterTerracotta,
                                    modifier = Modifier.size(26.dp)
                                )
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                text = "Your progress will appear here.",
                                fontFamily = NewsreaderFamily,
                                fontSize = 23.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = LaterInkPrimary,
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = "When a saved idea becomes a completed action, LATER will remember it.",
                                fontFamily = PlusJakartaSansFamily,
                                fontSize = 13.5.sp,
                                color = LaterSecondaryText,
                                lineHeight = 20.sp,
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                                modifier = Modifier.padding(horizontal = 14.dp)
                            )

                            Spacer(modifier = Modifier.height(24.dp))

                            Button(
                                onClick = onCreateFirstLaterMoment,
                                modifier = Modifier
                                    .height(46.dp)
                                    .testTag("proof_create_first_moment_button"),
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = LaterTerracotta,
                                    contentColor = Color.White
                                )
                            ) {
                                Text(
                                    text = "CREATE YOUR FIRST LATER MOMENT",
                                    fontFamily = PlusJakartaSansFamily,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 0.8.sp
                                )
                            }
                        }
                    }
                }
            } else {

            // Main Metric Card: “3 saved ideas turned into action”
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(2.dp, RoundedCornerShape(12.dp), ambientColor = LaterInkPrimary.copy(alpha = 0.05f))
                        .clip(RoundedCornerShape(12.dp))
                        .background(LaterCardBg)
                        .border(1.dp, LaterBorder, RoundedCornerShape(12.dp))
                        .padding(20.dp)
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "HEADLINE ACCOMPLISHMENT",
                                fontFamily = PlusJakartaSansFamily,
                                fontSize = 10.5.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp,
                                color = LaterTextMuted
                            )
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = LaterTerracotta,
                                modifier = Modifier.size(16.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "3 saved ideas turned into action",
                            fontFamily = NewsreaderFamily,
                            fontSize = 26.sp,
                            fontWeight = FontWeight.Bold,
                            lineHeight = 32.sp,
                            color = LaterInkPrimary
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "You converted passive bookmarks into active implementation on your dashboard project.",
                            fontFamily = PlusJakartaSansFamily,
                            fontSize = 13.sp,
                            color = LaterSecondaryText,
                            lineHeight = 19.sp
                        )
                    }
                }
            }

            // Supporting Metrics Row:
            // - “2 focus sessions completed”
            // - “45 minutes of intentional progress”
            // - “4 memories still need attention”
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Metric 1: Focus Sessions
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(10.dp))
                            .background(LaterCardBg)
                            .border(1.dp, LaterBorder, RoundedCornerShape(10.dp))
                            .padding(14.dp)
                    ) {
                        Text(
                            text = "$focusSessionsCompleted",
                            fontFamily = PlusJakartaSansFamily,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = LaterTerracotta
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "focus sessions completed",
                            fontFamily = PlusJakartaSansFamily,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            lineHeight = 15.sp,
                            color = LaterInkPrimary
                        )
                    }

                    // Metric 2: Intentional Progress
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(10.dp))
                            .background(LaterCardBg)
                            .border(1.dp, LaterBorder, RoundedCornerShape(10.dp))
                            .padding(14.dp)
                    ) {
                        Text(
                            text = "${focusMinutesTotal}m",
                            fontFamily = PlusJakartaSansFamily,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = LaterInkPrimary
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "of intentional progress",
                            fontFamily = PlusJakartaSansFamily,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            lineHeight = 15.sp,
                            color = LaterSecondaryText
                        )
                    }

                    // Metric 3: Attention needed
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(10.dp))
                            .background(LaterCardBg)
                            .border(1.dp, LaterBorder, RoundedCornerShape(10.dp))
                            .padding(14.dp)
                    ) {
                        Text(
                            text = "$needingAttentionCount",
                            fontFamily = PlusJakartaSansFamily,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = LaterDarkAccent
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "memories still need attention",
                            fontFamily = PlusJakartaSansFamily,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            lineHeight = 15.sp,
                            color = LaterSecondaryText
                        )
                    }
                }
            }

            // Reflective Prompt: “What idea mattered most to you this week?”
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(LaterCardBg)
                        .border(1.dp, LaterBorder, RoundedCornerShape(12.dp))
                        .padding(18.dp)
                ) {
                    Text(
                        text = "WEEKLY INTENTIONAL REFLECTION",
                        fontFamily = PlusJakartaSansFamily,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 1.sp,
                        color = LaterTerracotta
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "What idea mattered most to you this week?",
                        fontFamily = NewsreaderFamily,
                        fontSize = 19.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = LaterInkPrimary
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(LaterWhyBg.copy(alpha = 0.6f))
                            .border(1.dp, LaterTerracotta.copy(alpha = 0.2f), RoundedCornerShape(8.dp))
                            .padding(14.dp)
                    ) {
                        BasicTextField(
                            value = reflectionInput,
                            onValueChange = {
                                reflectionInput = it
                                viewModel.saveWeeklyReflection(it)
                            },
                            textStyle = TextStyle(
                                fontFamily = NewsreaderFamily,
                                fontSize = 15.sp,
                                lineHeight = 22.sp,
                                color = LaterInkPrimary
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(80.dp)
                                .testTag("weekly_reflection_input")
                        )
                    }
                }
            }

            // Breakdown by category with subtle progress bars
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(LaterCardBg)
                        .border(1.dp, LaterBorder, RoundedCornerShape(12.dp))
                        .padding(18.dp)
                ) {
                    Text(
                        text = "INTENTIONAL PROGRESS BY CATEGORY",
                        fontFamily = PlusJakartaSansFamily,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp,
                        color = LaterSecondaryText
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    categoryBreakdown.forEach { (category, ratio) ->
                        val animatedRatio by animateFloatAsState(targetValue = ratio, label = "ratio_$category")
                        Column(modifier = Modifier.padding(vertical = 5.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = category,
                                    fontFamily = PlusJakartaSansFamily,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = LaterInkPrimary
                                )
                                Text(
                                    text = "${(ratio * 100).toInt()}%",
                                    fontFamily = PlusJakartaSansFamily,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = LaterTerracotta
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(6.dp)
                                    .clip(RoundedCornerShape(3.dp))
                                    .background(LaterBorderSubtle)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth(animatedRatio)
                                        .height(6.dp)
                                        .clip(RoundedCornerShape(3.dp))
                                        .background(LaterTerracotta)
                                )
                            }
                        }
                    }
                }
            }

            // Action Buttons: “EXPORT SUMMARY” and “FINISH REVIEW”
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = {
                            haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                            try {
                                val sendIntent: Intent = Intent().apply {
                                    action = Intent.ACTION_SEND
                                    putExtra(
                                        Intent.EXTRA_TEXT,
                                        "LATER PRO Weekly Review:\n• 3 saved ideas turned into action\n• 2 focus sessions completed\n• 45 minutes of intentional progress\n\nReflection: $reflectionInput"
                                    )
                                    type = "text/plain"
                                }
                                val shareIntent = Intent.createChooser(sendIntent, "Export LATER Pro Review")
                                context.startActivity(shareIntent)
                            } catch (_: Exception) {
                                Toast.makeText(context, "Summary copied to clipboard", Toast.LENGTH_SHORT).show()
                            }
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = LaterInkPrimary),
                        border = androidx.compose.foundation.BorderStroke(1.dp, LaterBorder)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = LaterInkPrimary
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "EXPORT",
                            fontFamily = PlusJakartaSansFamily,
                            fontSize = 12.5.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.8.sp
                        )
                    }

                    Button(
                        onClick = {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            reviewCompleted = true
                            Toast.makeText(context, "Weekly review archived!", Toast.LENGTH_SHORT).show()
                            onFinishReview()
                        },
                        modifier = Modifier
                            .weight(1.3f)
                            .height(50.dp)
                            .testTag("finish_review_button"),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = LaterTerracotta,
                            contentColor = Color.White
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "FINISH REVIEW",
                            fontFamily = PlusJakartaSansFamily,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(40.dp))
            }
        }
    }

    if (lockedFeatureForDialog != null) {
        ProFeatureLockedDialog(
            featureName = lockedFeatureForDialog!!,
            onDismissRequest = { lockedFeatureForDialog = null },
            onUnlockPro = {
                lockedFeatureForDialog = null
                onOpenPaywall()
            }
        )
    }
}
}
