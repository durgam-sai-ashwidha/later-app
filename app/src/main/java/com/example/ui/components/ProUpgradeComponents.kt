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
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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

/**
 * Small Pro lock badge to indicate premium-only features.
 */
@Composable
fun ProLockBadge(
    modifier: Modifier = Modifier,
    label: String = "PRO"
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(4.dp))
            .background(LaterTerracottaLight)
            .border(1.dp, LaterTerracotta.copy(alpha = 0.35f), RoundedCornerShape(4.dp))
            .padding(horizontal = 5.dp, vertical = 2.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.Lock,
            contentDescription = "Pro required",
            tint = LaterTerracotta,
            modifier = Modifier.size(10.dp)
        )
        Spacer(modifier = Modifier.width(3.dp))
        Text(
            text = label,
            fontFamily = PlusJakartaSansFamily,
            fontSize = 9.sp,
            fontWeight = FontWeight.ExtraBold,
            letterSpacing = 0.6.sp,
            color = LaterTerracotta
        )
    }
}

/**
 * Upgrade Bottom Sheet shown when a Free user exceeds the 3 active Later Moments limit.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FreeLimitUpgradeBottomSheet(
    onDismissRequest: () -> Unit,
    onViewLaterPro: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        containerColor = LaterPaperBg,
        dragHandle = null,
        modifier = Modifier.testTag("free_limit_upgrade_sheet")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .imePadding()
                .padding(horizontal = 24.dp, vertical = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header icon badge
            Box(
                modifier = Modifier
                    .size(54.dp)
                    .clip(CircleShape)
                    .background(LaterTerracottaLight)
                    .border(1.dp, LaterTerracotta.copy(alpha = 0.3f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = null,
                    tint = LaterTerracotta,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            Text(
                text = "LATER PRO",
                fontFamily = PlusJakartaSansFamily,
                fontSize = 11.5.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 1.3.sp,
                color = LaterTerracotta
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Your future self has more to remember.",
                fontFamily = NewsreaderFamily,
                fontSize = 24.sp,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center,
                lineHeight = 28.sp,
                color = LaterInkPrimary
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "You have used your 3 active Later Moments. LATER Pro keeps every intention active, so the right memory can return at the right time.",
                fontFamily = PlusJakartaSansFamily,
                fontSize = 13.5.sp,
                textAlign = TextAlign.Center,
                lineHeight = 20.sp,
                color = LaterSecondaryText
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    onDismissRequest()
                    onViewLaterPro()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .shadow(3.dp, RoundedCornerShape(10.dp), ambientColor = LaterTerracotta.copy(alpha = 0.3f))
                    .testTag("view_later_pro_button"),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = LaterTerracotta,
                    contentColor = Color.White
                )
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "VIEW LATER PRO",
                        fontFamily = PlusJakartaSansFamily,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.8.sp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            TextButton(
                onClick = onDismissRequest,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp)
                    .testTag("not_now_button")
            ) {
                Text(
                    text = "NOT NOW",
                    fontFamily = PlusJakartaSansFamily,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.8.sp,
                    color = LaterTextMuted
                )
            }
        }
    }
}

/**
 * Concise upgrade dialog shown when tapping a locked Pro feature (e.g. routine/location triggers, extra projects).
 */
@Composable
fun ProFeatureLockedDialog(
    featureName: String,
    onDismissRequest: () -> Unit,
    onUnlockPro: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        containerColor = LaterPaperBg,
        shape = RoundedCornerShape(14.dp),
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = null,
                    tint = LaterTerracotta,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "This moment is waiting for Pro.",
                    fontFamily = NewsreaderFamily,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = LaterInkPrimary
                )
            }
        },
        text = {
            Text(
                text = "Unlock $featureName to bring more saved intentions back at the right time.",
                fontFamily = PlusJakartaSansFamily,
                fontSize = 13.5.sp,
                lineHeight = 19.sp,
                color = LaterSecondaryText
            )
        },
        confirmButton = {
            Button(
                onClick = {
                    onDismissRequest()
                    onUnlockPro()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = LaterTerracotta,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.testTag("unlock_later_pro_button")
            ) {
                Text(
                    text = "UNLOCK LATER PRO",
                    fontFamily = PlusJakartaSansFamily,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text(
                    text = "NOT NOW",
                    fontFamily = PlusJakartaSansFamily,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = LaterTextMuted
                )
            }
        }
    )
}
