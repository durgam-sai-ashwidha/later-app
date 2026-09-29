package com.example.ui.components

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.LaterBorder
import com.example.ui.theme.LaterCardBg
import com.example.ui.theme.LaterDarkAccent
import com.example.ui.theme.LaterInkPrimary
import com.example.ui.theme.LaterSecondaryText
import com.example.ui.theme.LaterTerracotta
import com.example.ui.theme.LaterTerracottaLight
import com.example.ui.theme.LaterWhyBg
import com.example.ui.theme.NewsreaderFamily
import com.example.ui.theme.PlusJakartaSansFamily
import kotlinx.coroutines.delay

@Composable
fun FocusSessionDialog(
    onDismissRequest: () -> Unit
) {
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current

    var remainingSeconds by remember { mutableIntStateOf(20 * 60) } // 20 minutes
    var isRunning by remember { mutableStateOf(false) }
    var isCompleted by remember { mutableStateOf(false) }

    LaunchedEffect(isRunning, remainingSeconds) {
        if (isRunning && remainingSeconds > 0) {
            delay(1000L)
            remainingSeconds -= 1
            if (remainingSeconds == 0) {
                isRunning = false
                isCompleted = true
            }
        }
    }

    val minutes = remainingSeconds / 60
    val seconds = remainingSeconds % 60
    val timeFormatted = String.format("%02d:%02d", minutes, seconds)

    AlertDialog(
        onDismissRequest = onDismissRequest,
        containerColor = LaterCardBg,
        shape = RoundedCornerShape(16.dp),
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Top close icon & title
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
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(LaterTerracotta)
                        )
                        Text(
                            text = "TODAY’S FOCUS SESSION",
                            fontFamily = PlusJakartaSansFamily,
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 1.1.sp,
                            color = LaterTerracotta
                        )
                    }

                    IconButton(
                        onClick = onDismissRequest,
                        modifier = Modifier.size(28.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = LaterSecondaryText,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Title
                Text(
                    text = "Redesign dashboard layout",
                    fontFamily = PlusJakartaSansFamily,
                    fontSize = 19.sp,
                    fontWeight = FontWeight.Bold,
                    color = LaterInkPrimary,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "“Save the why. Act when it matters.”",
                    fontFamily = NewsreaderFamily,
                    fontSize = 14.5.sp,
                    fontWeight = FontWeight.Medium,
                    color = LaterSecondaryText,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Timer Display Box
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(LaterTerracottaLight)
                        .border(1.dp, LaterTerracotta.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
                        .padding(horizontal = 24.dp, vertical = 14.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = timeFormatted,
                            fontFamily = PlusJakartaSansFamily,
                            fontSize = 38.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 1.sp,
                            color = LaterTerracotta
                        )

                        Text(
                            text = if (isCompleted) "FOCUS SESSION COMPLETE!" else if (isRunning) "SESSION IN PROGRESS" else "READY TO FOCUS",
                            fontFamily = PlusJakartaSansFamily,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 1.sp,
                            color = LaterDarkAccent
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Timer Controls
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Play / Pause Button
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(LaterTerracotta)
                            .clickable {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                if (isCompleted) {
                                    remainingSeconds = 20 * 60
                                    isCompleted = false
                                }
                                isRunning = !isRunning
                            }
                            .padding(horizontal = 20.dp, vertical = 10.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = if (isRunning) Icons.Default.Pause else Icons.Default.PlayArrow,
                                contentDescription = if (isRunning) "Pause" else "Start",
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (isRunning) "PAUSE" else "START TIMER",
                                fontFamily = PlusJakartaSansFamily,
                                fontSize = 12.5.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.8.sp,
                                color = Color.White
                            )
                        }
                    }

                    // Reset Button
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(Color.Transparent)
                            .border(1.dp, LaterBorder, CircleShape)
                            .clickable {
                                haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                isRunning = false
                                remainingSeconds = 20 * 60
                                isCompleted = false
                            }
                            .padding(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Reset timer",
                            tint = LaterSecondaryText,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Saved Reason Callout
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(LaterWhyBg.copy(alpha = 0.6f))
                        .padding(horizontal = 14.dp, vertical = 10.dp)
                ) {
                    Column {
                        Text(
                            text = "WHY YOU SAVED THIS",
                            fontFamily = PlusJakartaSansFamily,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 1.sp,
                            color = LaterTerracotta
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "“Need this for next sprint’s dashboard redesign—subgrid may solve the card-alignment issue.”",
                            fontFamily = NewsreaderFamily,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Medium,
                            lineHeight = 21.sp,
                            color = LaterInkPrimary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Link to resource
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(6.dp))
                        .border(1.dp, LaterBorder, RoundedCornerShape(6.dp))
                        .clickable {
                            try {
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://ishadeed.com/article/learn-css-subgrid/")).apply {
                                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                }
                                context.startActivity(intent)
                            } catch (_: Exception) {
                                Toast.makeText(context, "Opening CSS Subgrid Guide", Toast.LENGTH_SHORT).show()
                            }
                        }
                        .padding(horizontal = 12.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Modern CSS Grid & Subgrid Deep Dive",
                            fontFamily = PlusJakartaSansFamily,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = LaterInkPrimary
                        )
                        Text(
                            text = "ishadeed.com",
                            fontFamily = PlusJakartaSansFamily,
                            fontSize = 11.5.sp,
                            color = LaterSecondaryText
                        )
                    }

                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.OpenInNew,
                        contentDescription = "Open guide",
                        tint = LaterTerracotta,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    Toast.makeText(context, "Great focus! Progress recorded.", Toast.LENGTH_SHORT).show()
                    onDismissRequest()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = LaterTerracotta,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = "COMPLETE SESSION",
                    fontFamily = PlusJakartaSansFamily,
                    fontSize = 12.5.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.8.sp
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text(
                    text = "MINIMIZE",
                    fontFamily = PlusJakartaSansFamily,
                    fontSize = 12.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = LaterSecondaryText
                )
            }
        }
    )
}
