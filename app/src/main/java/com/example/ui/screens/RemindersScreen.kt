package com.example.ui.screens

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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Snooze
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
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
import com.example.ui.theme.LaterWhyBg
import com.example.ui.theme.NewsreaderFamily
import com.example.ui.theme.PlusJakartaSansFamily
import com.example.viewmodel.MemoryViewModel

@Composable
fun RemindersScreen(
    viewModel: MemoryViewModel,
    onStartFocusSession: (MemoryEntity) -> Unit,
    onNavigateToDetail: (String) -> Unit
) {
    val haptic = LocalHapticFeedback.current
    val reminders by viewModel.smartReminders.collectAsStateWithLifecycle()

    var editingReminderMemory by remember { mutableStateOf<MemoryEntity?>(null) }
    var changeReminderText by remember { mutableStateOf("") }

    if (editingReminderMemory != null) {
        val memory = editingReminderMemory!!
        AlertDialog(
            onDismissRequest = { editingReminderMemory = null },
            containerColor = LaterPaperBg,
            title = {
                Text(
                    text = "CHANGE SMART REMINDER",
                    fontFamily = PlusJakartaSansFamily,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp,
                    color = LaterTerracotta
                )
            },
            text = {
                Column {
                    Text(
                        text = memory.title,
                        fontFamily = PlusJakartaSansFamily,
                        fontSize = 14.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = LaterInkPrimary
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "When should LATER surface this context?",
                        fontFamily = PlusJakartaSansFamily,
                        fontSize = 12.sp,
                        color = LaterSecondaryText
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(LaterCardBg)
                            .border(1.dp, LaterBorder, RoundedCornerShape(8.dp))
                            .padding(12.dp)
                    ) {
                        BasicTextField(
                            value = changeReminderText,
                            onValueChange = { changeReminderText = it },
                            textStyle = TextStyle(
                                fontFamily = PlusJakartaSansFamily,
                                fontSize = 13.sp,
                                color = LaterInkPrimary
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                        viewModel.updateReminder(
                            memoryId = memory.id,
                            context = changeReminderText.ifBlank { memory.reminderContext },
                            time = System.currentTimeMillis() + 86400000L
                        )
                        editingReminderMemory = null
                    }
                ) {
                    Text(
                        text = "SAVE TRIGGER",
                        fontFamily = PlusJakartaSansFamily,
                        fontWeight = FontWeight.Bold,
                        color = LaterTerracotta
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = { editingReminderMemory = null }) {
                    Text("CANCEL", color = LaterTextMuted)
                }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(LaterPaperBg)
            .testTag("reminders_screen")
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
                        text = "LATER PRO",
                        fontFamily = PlusJakartaSansFamily,
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 1.2.sp,
                        color = LaterTerracotta
                    )
                    Text(
                        text = "SMART REMINDERS",
                        fontFamily = PlusJakartaSansFamily,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.SemiBold,
                        letterSpacing = 0.8.sp,
                        color = LaterTextMuted
                    )
                }
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(LaterCardBg)
                    .border(1.dp, LaterBorder, RoundedCornerShape(6.dp))
                    .padding(horizontal = 10.dp, vertical = 5.dp)
            ) {
                Text(
                    text = "${reminders.size} ACTIVE TRIGGERS",
                    fontFamily = PlusJakartaSansFamily,
                    fontSize = 10.5.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.5.sp,
                    color = LaterInkPrimary
                )
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 8.dp, bottom = 96.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header statement
            item {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "RIGHT TIME, NOT MORE TIME",
                        fontFamily = PlusJakartaSansFamily,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 1.2.sp,
                        color = LaterTerracotta
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "“LATER only reminds you when saved context becomes relevant.”",
                        fontFamily = NewsreaderFamily,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Medium,
                        lineHeight = 28.sp,
                        color = LaterInkPrimary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "No spam, no overdue notifications, no guilt. Just timely surfacing when it can turn into action.",
                        fontFamily = PlusJakartaSansFamily,
                        fontSize = 13.sp,
                        color = LaterSecondaryText,
                        lineHeight = 19.sp
                    )
                }
            }

            if (reminders.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(LaterCardBg)
                            .border(1.dp, LaterBorderSubtle, RoundedCornerShape(16.dp))
                            .padding(horizontal = 20.dp, vertical = 36.dp)
                            .testTag("moments_empty_state"),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(LaterTerracottaLight),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Schedule,
                                    contentDescription = null,
                                    tint = LaterTerracotta,
                                    modifier = Modifier.size(24.dp)
                                )
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            Text(
                                text = "No moments waiting.",
                                fontFamily = NewsreaderFamily,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = LaterInkPrimary
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            Text(
                                text = "When you save context for later, it will appear here.",
                                fontFamily = PlusJakartaSansFamily,
                                fontSize = 13.sp,
                                color = LaterSecondaryText,
                                lineHeight = 19.sp,
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                                modifier = Modifier.padding(horizontal = 16.dp)
                            )
                        }
                    }
                }
            } else {
                items(reminders, key = { it.id }) { memory ->
                    val reminderContext = memory.reminderContext ?: "Revisit this saved resource."
                    
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .shadow(2.dp, RoundedCornerShape(12.dp), ambientColor = LaterInkPrimary.copy(alpha = 0.04f))
                            .clip(RoundedCornerShape(12.dp))
                            .background(LaterCardBg)
                            .border(1.dp, LaterBorder, RoundedCornerShape(12.dp))
                            .padding(16.dp)
                            .testTag("reminder_card_${memory.id}")
                    ) {
                        // Top Context Row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.NotificationsActive,
                                    contentDescription = null,
                                    tint = LaterTerracotta,
                                    modifier = Modifier.size(14.dp)
                                )
                                Text(
                                    text = "CONTEXT TRIGGER",
                                    fontFamily = PlusJakartaSansFamily,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    letterSpacing = 1.sp,
                                    color = LaterTerracotta
                                )
                            }

                            // Dismiss button
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .clickable {
                                        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                        viewModel.dismissReminder(memory.id)
                                    }
                                    .padding(4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Dismiss reminder",
                                    tint = LaterTextMuted,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Smart reminder statement: “Your dashboard sprint starts tomorrow.” etc.
                        Text(
                            text = reminderContext,
                            fontFamily = NewsreaderFamily,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            lineHeight = 23.sp,
                            color = LaterInkPrimary
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // Associated memory preview
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(LaterWhyBg.copy(alpha = 0.5f))
                                .clickable { onNavigateToDetail(memory.id) }
                                .padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = (memory.category ?: "ARCHIVE").uppercase(),
                                    fontFamily = PlusJakartaSansFamily,
                                    fontSize = 9.5.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    letterSpacing = 0.8.sp,
                                    color = LaterSecondaryText
                                )
                                Text(
                                    text = memory.title,
                                    fontFamily = PlusJakartaSansFamily,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = LaterInkPrimary,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = null,
                                tint = LaterTextMuted,
                                modifier = Modifier.size(14.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Action Buttons: Start now, Snooze, Change reminder, Dismiss
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Primary: Start now
                            Box(
                                modifier = Modifier
                                    .weight(1.3f)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(LaterTerracotta)
                                    .clickable {
                                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                        onStartFocusSession(memory)
                                    }
                                    .padding(vertical = 9.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "START NOW →",
                                    fontFamily = PlusJakartaSansFamily,
                                    fontSize = 11.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 0.5.sp,
                                    color = Color.White
                                )
                            }

                            // Snooze
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(LaterTerracottaLight)
                                    .border(1.dp, LaterTerracotta.copy(alpha = 0.25f), RoundedCornerShape(8.dp))
                                    .clickable {
                                        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                        viewModel.snoozeReminder(memory.id, hours = 24)
                                    }
                                    .padding(vertical = 9.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Snooze,
                                        contentDescription = null,
                                        tint = LaterTerracotta,
                                        modifier = Modifier.size(12.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "SNOOZE",
                                        fontFamily = PlusJakartaSansFamily,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = LaterTerracotta
                                    )
                                }
                            }

                            // Change reminder
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(LaterCardBg)
                                    .border(1.dp, LaterBorder, RoundedCornerShape(8.dp))
                                    .clickable {
                                        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                        changeReminderText = reminderContext
                                        editingReminderMemory = memory
                                    }
                                    .padding(vertical = 9.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "EDIT",
                                    fontFamily = PlusJakartaSansFamily,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = LaterSecondaryText
                                )
                            }
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(40.dp))
            }
        }
    }
}
