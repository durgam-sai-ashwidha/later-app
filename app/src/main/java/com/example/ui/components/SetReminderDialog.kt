package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.MemoryEntity
import com.example.ui.theme.LaterBorder
import com.example.ui.theme.LaterCardBg
import com.example.ui.theme.LaterInkPrimary
import com.example.ui.theme.LaterPaperBg
import com.example.ui.theme.LaterSecondaryText
import com.example.ui.theme.LaterTerracotta
import com.example.ui.theme.LaterTextMuted
import com.example.ui.theme.PlusJakartaSansFamily

@Composable
fun SetReminderDialog(
    memory: MemoryEntity,
    onDismissRequest: () -> Unit,
    onSaveReminder: (String) -> Unit
) {
    var reminderText by remember {
        mutableStateOf(memory.reminderContext ?: "Revisit before next sprint")
    }

    val suggestions = listOf(
        "Your dashboard sprint starts tomorrow.",
        "You wanted to compare keyboards before the sale.",
        "Review this before the Thursday architecture meeting.",
        "Revisit next Monday morning."
    )

    AlertDialog(
        onDismissRequest = onDismissRequest,
        containerColor = LaterPaperBg,
        title = {
            Text(
                text = "SET CONTEXTUAL REMINDER",
                fontFamily = PlusJakartaSansFamily,
                fontSize = 12.5.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 1.sp,
                color = LaterTerracotta
            )
        },
        text = {
            Column {
                Text(
                    text = memory.title,
                    fontFamily = PlusJakartaSansFamily,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = LaterInkPrimary
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "When should LATER remind you why this matters?",
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
                        .padding(10.dp)
                ) {
                    BasicTextField(
                        value = reminderText,
                        onValueChange = { reminderText = it },
                        textStyle = TextStyle(
                            fontFamily = PlusJakartaSansFamily,
                            fontSize = 13.5.sp,
                            fontWeight = FontWeight.Medium,
                            color = LaterInkPrimary
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("set_reminder_input")
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "QUICK SUGGESTIONS",
                    fontFamily = PlusJakartaSansFamily,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.8.sp,
                    color = LaterTextMuted
                )
                Spacer(modifier = Modifier.height(4.dp))
                suggestions.forEach { sugg ->
                    Text(
                        text = "• $sugg",
                        fontFamily = PlusJakartaSansFamily,
                        fontSize = 11.5.sp,
                        color = LaterTerracotta,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(4.dp))
                            .clickable { reminderText = sugg }
                            .padding(vertical = 3.dp)
                    )
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = { onSaveReminder(reminderText) },
                modifier = Modifier.testTag("confirm_set_reminder_btn")
            ) {
                Text(
                    text = "SET REMINDER",
                    fontFamily = PlusJakartaSansFamily,
                    fontWeight = FontWeight.Bold,
                    color = LaterTerracotta
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text("CANCEL", color = LaterTextMuted)
            }
        }
    )
}
