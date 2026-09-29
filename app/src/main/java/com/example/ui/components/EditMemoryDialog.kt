package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
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
import com.example.ui.theme.LaterTerracottaLight
import com.example.ui.theme.LaterTextMuted
import com.example.ui.theme.LaterWhyBg
import com.example.ui.theme.NewsreaderFamily
import com.example.ui.theme.PlusJakartaSansFamily

@Composable
fun EditMemoryDialog(
    memory: MemoryEntity,
    onDismissRequest: () -> Unit,
    onSaveEdit: (MemoryEntity) -> Unit
) {
    var title by remember { mutableStateOf(memory.title) }
    var why by remember { mutableStateOf(memory.why) }
    var selectedCategory by remember { mutableStateOf(memory.category ?: "Learn") }
    var selectedState by remember { mutableStateOf(memory.state) }

    val categories = listOf("Learn", "Buy", "Try", "Reference", "Idea")
    val states = listOf("Needs action", "In progress", "Saved for later", "Completed")

    AlertDialog(
        onDismissRequest = onDismissRequest,
        containerColor = LaterPaperBg,
        title = {
            Text(
                text = "EDIT MEMORY",
                fontFamily = PlusJakartaSansFamily,
                fontSize = 13.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 1.sp,
                color = LaterTerracotta
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = "TITLE",
                    fontFamily = PlusJakartaSansFamily,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = LaterSecondaryText
                )
                Spacer(modifier = Modifier.height(4.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(LaterCardBg)
                        .border(1.dp, LaterBorder, RoundedCornerShape(8.dp))
                        .padding(10.dp)
                ) {
                    BasicTextField(
                        value = title,
                        onValueChange = { title = it },
                        textStyle = TextStyle(
                            fontFamily = PlusJakartaSansFamily,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = LaterInkPrimary
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "WHY YOU SAVED THIS",
                    fontFamily = PlusJakartaSansFamily,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = LaterTerracotta
                )
                Spacer(modifier = Modifier.height(4.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(LaterWhyBg.copy(alpha = 0.7f))
                        .border(1.dp, LaterBorder, RoundedCornerShape(8.dp))
                        .padding(10.dp)
                ) {
                    BasicTextField(
                        value = why,
                        onValueChange = { why = it },
                        textStyle = TextStyle(
                            fontFamily = NewsreaderFamily,
                            fontSize = 14.5.sp,
                            color = LaterInkPrimary,
                            lineHeight = 20.sp
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(70.dp)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "STATE",
                    fontFamily = PlusJakartaSansFamily,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = LaterSecondaryText
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    states.forEach { s ->
                        val isSelected = s.equals(selectedState, ignoreCase = true)
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(if (isSelected) LaterTerracottaLight else LaterCardBg)
                                .border(1.dp, if (isSelected) LaterTerracotta else LaterBorder, RoundedCornerShape(6.dp))
                                .clickable { selectedState = s }
                                .padding(horizontal = 8.dp, vertical = 5.dp)
                        ) {
                            Text(
                                text = s,
                                fontFamily = PlusJakartaSansFamily,
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) LaterTerracotta else LaterInkPrimary
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onSaveEdit(
                        memory.copy(
                            title = title.ifBlank { memory.title },
                            why = why.ifBlank { memory.why },
                            category = selectedCategory,
                            state = selectedState,
                            isCompleted = selectedState.equals("Completed", ignoreCase = true),
                            updatedAt = System.currentTimeMillis()
                        )
                    )
                },
                modifier = Modifier.testTag("confirm_edit_memory_btn")
            ) {
                Text(
                    text = "SAVE CHANGES",
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
