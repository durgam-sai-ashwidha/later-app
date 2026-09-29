package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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

@Composable
fun WelcomeOnboardingDialog(
    onSaveFirstWhy: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = LaterPaperBg,
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .testTag("first_launch_welcome_dialog"),
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
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
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "LATER",
                        fontFamily = PlusJakartaSansFamily,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 1.2.sp,
                        color = LaterTerracotta
                    )
                }

                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier.size(28.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Dismiss",
                        tint = LaterTextMuted
                    )
                }
            }
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                Spacer(modifier = Modifier.height(4.dp))

                // Header: “Your future self is waiting.”
                Text(
                    text = "Your future self is waiting.",
                    fontFamily = NewsreaderFamily,
                    fontSize = 25.sp,
                    fontWeight = FontWeight.SemiBold,
                    lineHeight = 31.sp,
                    color = LaterInkPrimary
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Body: “Save something you will need later—and give it a reason to come back.”
                Text(
                    text = "Save something you will need later—and give it a reason to come back.",
                    fontFamily = PlusJakartaSansFamily,
                    fontSize = 13.5.sp,
                    lineHeight = 20.sp,
                    color = LaterSecondaryText
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Subtle equation card
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(LaterCardBg)
                        .border(1.dp, LaterBorderSubtle, RoundedCornerShape(10.dp))
                        .padding(14.dp)
                ) {
                    Column {
                        Text(
                            text = "THE INTENT EQUATION",
                            fontFamily = PlusJakartaSansFamily,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 1.sp,
                            color = LaterTerracotta
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "Item + Personal Why + Trigger + Tiny Action = Follow-through",
                            fontFamily = PlusJakartaSansFamily,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = LaterInkPrimary
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(6.dp))
                                .background(LaterWhyBg)
                                .padding(horizontal = 10.dp, vertical = 7.dp)
                        ) {
                            Text(
                                text = "“Need this for the dashboard redesign sprint—subgrid solves card alignment.”",
                                fontFamily = NewsreaderFamily,
                                fontSize = 12.5.sp,
                                color = LaterInkPrimary,
                                lineHeight = 17.sp
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onSaveFirstWhy,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("welcome_save_first_why_button"),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = LaterTerracotta,
                    contentColor = Color.White
                )
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "SAVE YOUR FIRST WHY",
                        fontFamily = PlusJakartaSansFamily,
                        fontSize = 12.5.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 0.8.sp
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                        modifier = Modifier.size(15.dp)
                    )
                }
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Explore the space first",
                    fontFamily = PlusJakartaSansFamily,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = LaterTextMuted
                )
            }
        }
    )
}
