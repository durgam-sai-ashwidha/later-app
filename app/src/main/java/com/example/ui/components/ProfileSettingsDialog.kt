package com.example.ui.components

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
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
import com.example.ui.theme.NewsreaderFamily
import com.example.ui.theme.PlusJakartaSansFamily

@Composable
fun ProfileSettingsDialog(
    memoryCount: Int,
    streak: Int,
    focusSessionsCompleted: Int,
    isDemoMode: Boolean,
    isPro: Boolean = false,
    userMemoryCount: Int = 0,
    onLoadDemoWorkspace: () -> Unit,
    onClearDemoWorkspace: () -> Unit,
    onOpenPaywall: () -> Unit = {},
    onRestorePurchases: () -> Unit = {},
    onDismissRequest: () -> Unit
) {
    var showDemoConfirmDialog by remember { mutableStateOf(false) }

    if (showDemoConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showDemoConfirmDialog = false },
            containerColor = LaterPaperBg,
            shape = RoundedCornerShape(14.dp),
            title = {
                Text(
                    text = "Load demo workspace?",
                    fontFamily = PlusJakartaSansFamily,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = LaterInkPrimary
                )
            },
            text = {
                Text(
                    text = "You have $userMemoryCount custom memory saved. Loading demo data will add the 4 presentation memories without erasing your data. You can clear them anytime.",
                    fontFamily = PlusJakartaSansFamily,
                    fontSize = 13.sp,
                    lineHeight = 18.sp,
                    color = LaterSecondaryText
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showDemoConfirmDialog = false
                        onLoadDemoWorkspace()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = LaterTerracotta,
                        contentColor = Color.White
                    )
                ) {
                    Text("Load demo data")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDemoConfirmDialog = false }) {
                    Text("Cancel", color = LaterTextMuted)
                }
            }
        )
    }

    AlertDialog(
        onDismissRequest = onDismissRequest,
        containerColor = LaterPaperBg,
        shape = RoundedCornerShape(16.dp),
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    LaterLogoMark(
                        size = 22.dp,
                        tint = LaterTerracotta,
                        foldTint = LaterDarkAccent,
                        cutoutColor = LaterPaperBg
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "SETTINGS",
                        fontFamily = PlusJakartaSansFamily,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 1.2.sp,
                        color = LaterTerracotta
                    )

                    // Small unobtrusive DEMO indicator ONLY inside Settings
                    if (isDemoMode) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(LaterTerracotta)
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                                .testTag("settings_demo_badge")
                        ) {
                            Text(
                                text = "DEMO",
                                fontFamily = PlusJakartaSansFamily,
                                fontSize = 9.5.sp,
                                fontWeight = FontWeight.ExtraBold,
                                letterSpacing = 0.8.sp,
                                color = Color.White
                            )
                        }
                    }
                }

                IconButton(
                    onClick = onDismissRequest,
                    modifier = Modifier.size(28.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = LaterTextMuted
                    )
                }
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                // =============================================================
                // 1. REVENUECAT LATER PRO CARD AT TOP OF SETTINGS
                // =============================================================
                if (!isPro) {
                    // Free User Upgrade Card
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(LaterCardBg)
                            .border(1.5.dp, LaterTerracotta.copy(alpha = 0.45f), RoundedCornerShape(12.dp))
                            .clickable {
                                onDismissRequest()
                                onOpenPaywall()
                            }
                            .padding(14.dp)
                            .testTag("settings_pro_upgrade_card")
                    ) {
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Lock,
                                        contentDescription = null,
                                        tint = LaterTerracotta,
                                        modifier = Modifier.size(15.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "LATER PRO",
                                        fontFamily = PlusJakartaSansFamily,
                                        fontSize = 11.5.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        letterSpacing = 1.1.sp,
                                        color = LaterTerracotta
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(6.dp))

                            Text(
                                text = "Keep every saved intention active.",
                                fontFamily = NewsreaderFamily,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = LaterInkPrimary
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            Row(
                                modifier = Modifier.padding(vertical = 2.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null,
                                    tint = LaterTerracotta,
                                    modifier = Modifier.size(13.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Unlimited active Later Moments",
                                    fontFamily = PlusJakartaSansFamily,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = LaterSecondaryText
                                )
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            Button(
                                onClick = {
                                    onDismissRequest()
                                    onOpenPaywall()
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(38.dp)
                                    .testTag("settings_upgrade_to_pro_button"),
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = LaterTerracotta,
                                    contentColor = Color.White
                                )
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = "UPGRADE TO PRO",
                                        fontFamily = PlusJakartaSansFamily,
                                        fontSize = 11.5.sp,
                                        fontWeight = FontWeight.Bold,
                                        letterSpacing = 0.8.sp
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                        contentDescription = null,
                                        modifier = Modifier.size(14.dp)
                                    )
                                }
                            }
                        }
                    }
                } else {
                    // Pro Active Card
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(LaterCardBg)
                            .border(1.5.dp, LaterTerracotta, RoundedCornerShape(12.dp))
                            .padding(14.dp)
                            .testTag("settings_pro_active_card")
                    ) {
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "LATER PRO",
                                    fontFamily = PlusJakartaSansFamily,
                                    fontSize = 11.5.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    letterSpacing = 1.1.sp,
                                    color = LaterTerracotta
                                )

                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(LaterTerracottaLight)
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = "PRO ACTIVE",
                                        fontFamily = PlusJakartaSansFamily,
                                        fontSize = 9.5.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        letterSpacing = 0.8.sp,
                                        color = LaterTerracotta
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = "Unlimited active Later Moments unlocked.",
                                fontFamily = NewsreaderFamily,
                                fontSize = 17.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = LaterInkPrimary
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                TextButton(
                                    onClick = onRestorePurchases,
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(36.dp)
                                        .testTag("settings_restore_purchases_button")
                                ) {
                                    Text(
                                        text = "RESTORE PURCHASES",
                                        fontFamily = PlusJakartaSansFamily,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = LaterTerracotta
                                    )
                                }

                                OutlinedButton(
                                    onClick = {
                                        onDismissRequest()
                                        onOpenPaywall()
                                    },
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(36.dp)
                                        .testTag("settings_manage_subscription_button"),
                                    shape = RoundedCornerShape(6.dp),
                                    colors = ButtonDefaults.outlinedButtonColors(contentColor = LaterInkPrimary)
                                ) {
                                    Text(
                                        text = "MANAGE SUBSCRIPTION",
                                        fontFamily = PlusJakartaSansFamily,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // User Profile Card
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(LaterCardBg)
                        .border(1.dp, LaterBorder, RoundedCornerShape(10.dp))
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(LaterTerracottaLight),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (isDemoMode) "D" else "A",
                            fontFamily = NewsreaderFamily,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = LaterTerracotta
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = if (isDemoMode) "Demo Workspace" else "Alex Chen",
                                fontFamily = PlusJakartaSansFamily,
                                fontSize = 14.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = LaterInkPrimary
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Icon(
                                imageVector = Icons.Default.Verified,
                                contentDescription = "Active",
                                tint = LaterTerracotta,
                                modifier = Modifier.size(15.dp)
                            )
                        }
                        Text(
                            text = if (isDemoMode) "Presentation & feature preview" else "alex@intent.studio",
                            fontFamily = PlusJakartaSansFamily,
                            fontSize = 11.sp,
                            color = LaterSecondaryText
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Stats Row (Only displays numbers if they exist, never numerical zero-state streaks)
                if (memoryCount > 0 || streak > 0 || focusSessionsCompleted > 0) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .background(LaterCardBg)
                                .border(1.dp, LaterBorder, RoundedCornerShape(8.dp))
                                .padding(10.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "$memoryCount",
                                fontFamily = PlusJakartaSansFamily,
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold,
                                color = LaterInkPrimary
                            )
                            Text(
                                text = "Memories",
                                fontFamily = PlusJakartaSansFamily,
                                fontSize = 10.sp,
                                color = LaterTextMuted
                            )
                        }

                        if (streak > 0) {
                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(LaterCardBg)
                                    .border(1.dp, LaterBorder, RoundedCornerShape(8.dp))
                                    .padding(10.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "$streak days",
                                    fontFamily = PlusJakartaSansFamily,
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = LaterTerracotta
                                )
                                Text(
                                    text = "Streak",
                                    fontFamily = PlusJakartaSansFamily,
                                    fontSize = 10.sp,
                                    color = LaterTextMuted
                                )
                            }
                        }

                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .background(LaterCardBg)
                                .border(1.dp, LaterBorder, RoundedCornerShape(8.dp))
                                .padding(10.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "$focusSessionsCompleted",
                                fontFamily = PlusJakartaSansFamily,
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold,
                                color = LaterInkPrimary
                            )
                            Text(
                                text = "Focuses",
                                fontFamily = PlusJakartaSansFamily,
                                fontSize = 10.sp,
                                color = LaterTextMuted
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(14.dp))
                }

                // =============================================================
                // DEVELOPER / WORKSPACE SETTINGS: DEMO WORKSPACE
                // =============================================================
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(LaterCardBg)
                        .border(1.dp, LaterBorder, RoundedCornerShape(10.dp))
                        .padding(14.dp)
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Science,
                                    contentDescription = null,
                                    tint = LaterTerracotta,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "DEMO WORKSPACE",
                                    fontFamily = PlusJakartaSansFamily,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    letterSpacing = 1.sp,
                                    color = LaterTerracotta
                                )
                            }

                            if (isDemoMode) {
                                Text(
                                    text = "Active",
                                    fontFamily = PlusJakartaSansFamily,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = LaterTerracotta
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = if (isDemoMode) {
                                "Demo mode is active with 4 sample memories (CSS Grid, Ergonomic Keyboard, Mobility Routine, Design Tokens)."
                            } else {
                                "Test the full intent follow-through flow with curated sample memories and presentation metrics."
                            },
                            fontFamily = PlusJakartaSansFamily,
                            fontSize = 12.sp,
                            lineHeight = 17.sp,
                            color = LaterSecondaryText
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        if (!isDemoMode) {
                            Button(
                                onClick = {
                                    if (userMemoryCount > 0) {
                                        showDemoConfirmDialog = true
                                    } else {
                                        onLoadDemoWorkspace()
                                    }
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(40.dp)
                                    .testTag("load_demo_workspace_button"),
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = LaterTerracotta,
                                    contentColor = Color.White
                                )
                            ) {
                                Text(
                                    text = "Load demo workspace",
                                    fontFamily = PlusJakartaSansFamily,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        } else {
                            OutlinedButton(
                                onClick = onClearDemoWorkspace,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(40.dp)
                                    .testTag("clear_demo_workspace_button"),
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    contentColor = LaterTerracotta
                                )
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.DeleteSweep,
                                        contentDescription = null,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "Clear demo workspace and start fresh",
                                        fontFamily = PlusJakartaSansFamily,
                                        fontSize = 11.5.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Test-only button: "Open LATER Pro paywall" (Requirement 7)
                        OutlinedButton(
                            onClick = {
                                onDismissRequest()
                                onOpenPaywall()
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(40.dp)
                                .testTag("open_later_pro_paywall_button"),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = LaterInkPrimary)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Payments,
                                    contentDescription = null,
                                    tint = LaterTerracotta,
                                    modifier = Modifier.size(15.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "OPEN LATER PRO PAYWALL",
                                    fontFamily = PlusJakartaSansFamily,
                                    fontSize = 11.5.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Privacy assurance note
                Text(
                    text = "• Fresh mode never shows sample memories\n• Offline-first SQLite persistence with zero telemetry",
                    fontFamily = PlusJakartaSansFamily,
                    fontSize = 11.sp,
                    lineHeight = 16.sp,
                    color = LaterTextMuted
                )
            }
        },
        confirmButton = {
            TextButton(onClick = onDismissRequest) {
                Text(
                    text = "DONE",
                    fontFamily = PlusJakartaSansFamily,
                    fontWeight = FontWeight.Bold,
                    color = LaterTerracotta
                )
            }
        }
    )
}
