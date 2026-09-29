package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.MemoryEntity
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
import kotlinx.coroutines.delay

@Composable
fun FocusSessionScreen(
    memory: MemoryEntity?,
    viewModel: MemoryViewModel,
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current

    val resourceTitle = memory?.title ?: "Modern CSS Grid & Subgrid Deep Dive"
    val resourceWhy = memory?.why ?: "Need this for next sprint’s dashboard redesign—subgrid may solve the card-alignment issue."
    val resourceUrl = memory?.content ?: "https://ishadeed.com/article/learn-css-subgrid/"

    val initialSeconds = 20 * 60 // 20 minutes
    var secondsRemaining by remember { mutableIntStateOf(initialSeconds) }
    var isTimerRunning by remember { mutableStateOf(true) }
    var isCompleted by remember { mutableStateOf(false) }
    var showStopConfirmDialog by remember { mutableStateOf(false) }
    var focusNoteInput by remember { mutableStateOf("") }
    var isAddingNote by remember { mutableStateOf(false) }

    // Three-step action plan checklist
    val stepsCompleted = remember { mutableStateListOf(false, false, false) }
    val steps = listOf(
        "Read the Subgrid section",
        "Identify one dashboard area to improve",
        "Create a small implementation note"
    )

    BackHandler {
        onNavigateBack()
    }

    LaunchedEffect(isTimerRunning, isCompleted) {
        while (isTimerRunning && secondsRemaining > 0 && !isCompleted) {
            delay(1000L)
            secondsRemaining -= 1
            if (secondsRemaining == 0) {
                isCompleted = true
                viewModel.completeFocusSession(
                    memoryId = memory?.id ?: "sample-1",
                    durationMinutes = 20,
                    note = focusNoteInput
                )
            }
        }
    }

    val minutes = secondsRemaining / 60
    val seconds = secondsRemaining % 60
    val timeFormatted = String.format("%02d:%02d", minutes, seconds)
    val progress = (secondsRemaining.toFloat() / initialSeconds.toFloat()).coerceIn(0f, 1f)
    val animatedProgress by animateFloatAsState(targetValue = progress, label = "timer_progress")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(LaterPaperBg)
            .statusBarsPadding()
            .navigationBarsPadding()
            .imePadding()
            .padding(horizontal = 20.dp, vertical = 12.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Top Bar: Header “LATER FOCUS” & Close/Back
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(
                    onClick = onNavigateBack,
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Leave focus session",
                        tint = LaterInkPrimary
                    )
                }
                Spacer(modifier = Modifier.width(6.dp))
                LaterLogoMark(
                    size = 18.dp,
                    tint = LaterTerracotta,
                    foldTint = LaterDarkAccent,
                    cutoutColor = LaterPaperBg
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "LATER FOCUS",
                    fontFamily = PlusJakartaSansFamily,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 1.2.sp,
                    color = LaterTerracotta
                )
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(LaterTerracottaLight)
                    .border(1.dp, LaterTerracotta.copy(alpha = 0.3f), RoundedCornerShape(6.dp))
                    .padding(horizontal = 10.dp, vertical = 5.dp)
            ) {
                Text(
                    text = "DISTRACTION FREE",
                    fontFamily = PlusJakartaSansFamily,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.8.sp,
                    color = LaterTerracotta
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (!isCompleted) {
            // Memory Context Card with prominent elegant serif quote
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(2.dp, RoundedCornerShape(14.dp), ambientColor = LaterInkPrimary.copy(alpha = 0.05f))
                    .clip(RoundedCornerShape(14.dp))
                    .background(LaterCardBg)
                    .border(1.dp, LaterBorder, RoundedCornerShape(14.dp))
                    .padding(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "SAVED CONTEXT & OBJECTIVE",
                        fontFamily = PlusJakartaSansFamily,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 1.2.sp,
                        color = LaterTerracotta
                    )

                    Text(
                        text = "WHY FIRST",
                        fontFamily = PlusJakartaSansFamily,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.8.sp,
                        color = LaterTextMuted
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                // Resource title in clean sans-serif
                Text(
                    text = resourceTitle,
                    fontFamily = PlusJakartaSansFamily,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 23.sp,
                    color = LaterInkPrimary
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Saved reason in prominent elegant high-contrast serif quote
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(LaterWhyBg.copy(alpha = 0.85f))
                        .border(1.dp, LaterBorderSubtle, RoundedCornerShape(10.dp))
                        .padding(16.dp)
                ) {
                    Text(
                        text = "“",
                        fontFamily = NewsreaderFamily,
                        fontSize = 32.sp,
                        lineHeight = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = LaterTerracotta
                    )

                    Text(
                        text = resourceWhy,
                        fontFamily = NewsreaderFamily,
                        fontSize = 16.5.sp,
                        fontWeight = FontWeight.Medium,
                        lineHeight = 25.sp,
                        letterSpacing = 0.15.sp,
                        color = LaterInkPrimary
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "— YOUR SAVED WHY",
                        fontFamily = PlusJakartaSansFamily,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 1.sp,
                        color = LaterSecondaryText
                    )
                }

                if (resourceUrl.startsWith("http")) {
                    Spacer(modifier = Modifier.height(14.dp))
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .clickable {
                                try {
                                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(resourceUrl))
                                    context.startActivity(intent)
                                } catch (_: Exception) {}
                            }
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.OpenInNew,
                            contentDescription = null,
                            tint = LaterTerracotta,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "OPEN SAVED RESOURCE ↗",
                            fontFamily = PlusJakartaSansFamily,
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.6.sp,
                            color = LaterTerracotta
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 20-minute Countdown Timer with Animated Circular Ring
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(190.dp)
            ) {
                CircularProgressIndicator(
                    progress = { 1f },
                    modifier = Modifier.fillMaxSize(),
                    color = LaterBorderSubtle,
                    strokeWidth = 6.dp
                )

                CircularProgressIndicator(
                    progress = { animatedProgress },
                    modifier = Modifier.fillMaxSize(),
                    color = LaterTerracotta,
                    strokeWidth = 6.dp
                )

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = timeFormatted,
                        fontFamily = PlusJakartaSansFamily,
                        fontSize = 38.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = (-0.5).sp,
                        color = LaterInkPrimary
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = if (isTimerRunning) "FOCUSING" else "PAUSED",
                        fontFamily = PlusJakartaSansFamily,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.2.sp,
                        color = if (isTimerRunning) LaterTerracotta else LaterSecondaryText
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Timer Controls: Reset, Play/Pause, and Stop
            Row(
                horizontalArrangement = Arrangement.spacedBy(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 1. Reset Control
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(LaterCardBg)
                            .border(1.dp, LaterBorder, CircleShape)
                            .clickable {
                                haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                secondsRemaining = initialSeconds
                                isTimerRunning = false
                            }
                            .testTag("focus_reset_button"),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Reset timer to 20 minutes",
                            tint = LaterSecondaryText,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "RESET",
                        fontFamily = PlusJakartaSansFamily,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = LaterTextMuted
                    )
                }

                // 2. Play/Pause Primary Control
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(26.dp))
                            .background(LaterTerracotta)
                            .clickable {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                isTimerRunning = !isTimerRunning
                            }
                            .padding(horizontal = 22.dp, vertical = 12.dp)
                            .testTag("focus_play_pause_button"),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = if (isTimerRunning) Icons.Default.Pause else Icons.Default.PlayArrow,
                            contentDescription = if (isTimerRunning) "Pause timer" else "Resume timer",
                            tint = Color.White,
                            modifier = Modifier.size(22.dp)
                        )
                        Text(
                            text = if (isTimerRunning) "PAUSE" else "RESUME",
                            fontFamily = PlusJakartaSansFamily,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 0.8.sp,
                            color = Color.White
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = if (isTimerRunning) "ACTIVE" else "ON HOLD",
                        fontFamily = PlusJakartaSansFamily,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = LaterTerracotta
                    )
                }

                // 3. Stop Control
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(LaterCardBg)
                            .border(1.dp, LaterBorder, CircleShape)
                            .clickable {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                isTimerRunning = false
                                showStopConfirmDialog = true
                            }
                            .testTag("focus_stop_button"),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Stop,
                            contentDescription = "Stop session",
                            tint = LaterTerracotta,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "STOP",
                        fontFamily = PlusJakartaSansFamily,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = LaterTextMuted
                    )
                }
            }

            Spacer(modifier = Modifier.height(26.dp))

            // Three-step action plan
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(LaterCardBg)
                    .border(1.dp, LaterBorder, RoundedCornerShape(12.dp))
                    .padding(16.dp)
            ) {
                Text(
                    text = "THREE-STEP ACTION PLAN",
                    fontFamily = PlusJakartaSansFamily,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 1.sp,
                    color = LaterSecondaryText
                )

                Spacer(modifier = Modifier.height(10.dp))

                steps.forEachIndexed { index, step ->
                    val checked = stepsCompleted[index]
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .clickable {
                                haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                stepsCompleted[index] = !checked
                            }
                            .padding(vertical = 8.dp, horizontal = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(22.dp)
                                .clip(RoundedCornerShape(5.dp))
                                .background(if (checked) LaterTerracotta else Color.Transparent)
                                .border(
                                    1.5.dp,
                                    if (checked) LaterTerracotta else LaterBorder,
                                    RoundedCornerShape(5.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            if (checked) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(15.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Text(
                            text = "${index + 1}. $step",
                            fontFamily = PlusJakartaSansFamily,
                            fontSize = 13.5.sp,
                            fontWeight = if (checked) FontWeight.SemiBold else FontWeight.Medium,
                            color = if (checked) LaterTextMuted else LaterInkPrimary
                        )
                    }

                    if (index < steps.size - 1) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(1.dp)
                                .background(LaterBorderSubtle)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Button: “MARK AS COMPLETE”
            Button(
                onClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    isCompleted = true
                    viewModel.completeFocusSession(
                        memoryId = memory?.id ?: "sample-1",
                        durationMinutes = 20,
                        note = focusNoteInput
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .testTag("mark_focus_complete_button"),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = LaterTerracotta,
                    contentColor = Color.White
                )
            ) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "MARK AS COMPLETE",
                    fontFamily = PlusJakartaSansFamily,
                    fontSize = 13.5.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
            }
        } else {
            // Warm Success State at completion:
            // - “You turned a saved idea into progress.”
            // - “2-day follow-through streak”
            // - Buttons: “SAVE A NOTE” and “BACK TO TODAY”
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(72.dp)
                        .clip(CircleShape)
                        .background(LaterTerracottaLight)
                        .border(1.5.dp, LaterTerracotta, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Session completed",
                        tint = LaterTerracotta,
                        modifier = Modifier.size(38.dp)
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "FOCUS SESSION COMPLETE",
                    fontFamily = PlusJakartaSansFamily,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 1.4.sp,
                    color = LaterTerracotta
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "You turned a saved idea into progress.",
                    fontFamily = NewsreaderFamily,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    color = LaterInkPrimary,
                    lineHeight = 32.sp
                )

                Spacer(modifier = Modifier.height(10.dp))

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(LaterTerracottaLight)
                        .padding(horizontal = 14.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = "🔥 2-day follow-through streak",
                        fontFamily = PlusJakartaSansFamily,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = LaterTerracotta
                    )
                }

                Spacer(modifier = Modifier.height(28.dp))

                if (isAddingNote) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(LaterCardBg)
                            .border(1.dp, LaterBorder, RoundedCornerShape(10.dp))
                            .padding(14.dp)
                    ) {
                        Text(
                            text = "IMPLEMENTATION NOTE",
                            fontFamily = PlusJakartaSansFamily,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = LaterSecondaryText
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        BasicTextField(
                            value = focusNoteInput,
                            onValueChange = { focusNoteInput = it },
                            textStyle = TextStyle(
                                fontFamily = PlusJakartaSansFamily,
                                fontSize = 14.sp,
                                color = LaterInkPrimary
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(80.dp)
                                .testTag("focus_note_input")
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(
                            onClick = {
                                haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                isAddingNote = false
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = LaterTerracotta),
                            shape = RoundedCornerShape(6.dp),
                            modifier = Modifier.align(Alignment.End)
                        ) {
                            Text("SAVE NOTE")
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }

                // Buttons: “SAVE A NOTE” and “BACK TO TODAY”
                if (!isAddingNote) {
                    OutlinedButton(
                        onClick = {
                            isAddingNote = true
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = LaterInkPrimary),
                        border = androidx.compose.foundation.BorderStroke(1.dp, LaterBorder)
                    ) {
                        Text(
                            text = "SAVE A NOTE",
                            fontFamily = PlusJakartaSansFamily,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.8.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                }

                Button(
                    onClick = onNavigateBack,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("back_to_today_button"),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = LaterTerracotta,
                        contentColor = Color.White
                    )
                ) {
                    Text(
                        text = "BACK TO TODAY",
                        fontFamily = PlusJakartaSansFamily,
                        fontSize = 13.5.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                }
            }
        }
    }

    // Stop Confirmation Dialog
    if (showStopConfirmDialog) {
        androidx.compose.material3.AlertDialog(
            onDismissRequest = {
                showStopConfirmDialog = false
                isTimerRunning = true
            },
            containerColor = LaterPaperBg,
            title = {
                Text(
                    text = "END FOCUS SESSION?",
                    fontFamily = PlusJakartaSansFamily,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 1.sp,
                    color = LaterTerracotta
                )
            },
            text = {
                Column {
                    Text(
                        text = "You still have $timeFormatted remaining in this focus sprint.",
                        fontFamily = PlusJakartaSansFamily,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = LaterInkPrimary
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Would you like to mark your intentional progress as completed, or discard this session?",
                        fontFamily = PlusJakartaSansFamily,
                        fontSize = 12.5.sp,
                        color = LaterSecondaryText
                    )
                }
            },
            confirmButton = {
                androidx.compose.material3.TextButton(
                    onClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        showStopConfirmDialog = false
                        isCompleted = true
                        val elapsedMinutes = ((initialSeconds - secondsRemaining) / 60).coerceAtLeast(1)
                        viewModel.completeFocusSession(
                            memoryId = memory?.id ?: "sample-1",
                            durationMinutes = elapsedMinutes,
                            note = focusNoteInput
                        )
                    }
                ) {
                    Text(
                        text = "MARK AS COMPLETE",
                        fontFamily = PlusJakartaSansFamily,
                        fontWeight = FontWeight.Bold,
                        color = LaterTerracotta
                    )
                }
            },
            dismissButton = {
                Row {
                    androidx.compose.material3.TextButton(
                        onClick = {
                            showStopConfirmDialog = false
                            onNavigateBack()
                        }
                    ) {
                        Text("EXIT", color = LaterTextMuted)
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                    androidx.compose.material3.TextButton(
                        onClick = {
                            showStopConfirmDialog = false
                            isTimerRunning = true
                        }
                    ) {
                        Text("RESUME", color = LaterInkPrimary)
                    }
                }
            }
        )
    }
}
