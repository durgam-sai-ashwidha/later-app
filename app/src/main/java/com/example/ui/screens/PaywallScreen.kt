package com.example.ui.screens

import android.widget.Toast
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.components.BookmarkCorner
import com.example.ui.components.LaterLogoMark
import com.example.ui.theme.LaterBorder
import com.example.ui.theme.LaterBorderSubtle
import com.example.ui.theme.LaterCardBg
import com.example.ui.theme.LaterDarkAccent
import com.example.ui.theme.LaterInkPrimary
import com.example.ui.theme.LaterPaperBg
import com.example.ui.theme.LaterTerracotta
import com.example.ui.theme.LaterTerracottaLight
import com.example.ui.theme.LaterTextMuted
import com.example.ui.theme.LaterWarmGray
import com.example.ui.theme.NewsreaderFamily
import com.example.ui.theme.PlusJakartaSansFamily
import com.example.viewmodel.MemoryViewModel

/**
 * 7. PREMIUM SCREEN & VISUAL HIERARCHY
 *
 * Strict Visual Hierarchy:
 * 1. Most important: KEEP EVERY REASON
 * 2. Second: Your first 50 memories are free.
 * 3. Third: Pro removes the limit.
 * 4. Benefits:
 *    PRO
 *    ✓ Unlimited memories
 *    ✓ Keep building your archive
 *    ✓ Full WHY-first search
 *    ✓ Your saved context stays yours
 * 5. Purchase Action: [ CONTINUE WITH PRO ] (Terracotta accent)
 * 6. Restore: Restore purchases
 * 7. Microcopy: "50 memories is enough to experience LATER. Pro is for keeping it."
 *
 * RevenueCat-Ready UX:
 * - Entitlement: pro
 * - Non-blocking: failures or cancellation never lock existing memories or search.
 */
@Composable
fun PaywallScreen(
    viewModel: MemoryViewModel,
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    val isPro by viewModel.isProSubscriber.collectAsStateWithLifecycle()
    val totalCount by viewModel.memoryCount.collectAsStateWithLifecycle()

    Scaffold(
        containerColor = LaterPaperBg
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 640.dp)
                    .padding(horizontal = 26.dp, vertical = 20.dp)
            ) {
                // Top Dismiss Action
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    LaterLogoMark(
                        size = 24.dp,
                        tint = LaterTerracotta,
                        foldTint = LaterDarkAccent,
                        cutoutColor = LaterPaperBg
                    )

                    IconButton(
                        onClick = onNavigateBack,
                        modifier = Modifier.testTag("paywall_close_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = LaterInkPrimary,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(28.dp))

                // 1. Most Important Text: KEEP EVERY REASON
                Text(
                    text = "KEEP EVERY REASON",
                    fontFamily = PlusJakartaSansFamily,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 2.sp,
                    color = LaterInkPrimary
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "You've started building your archive.",
                    fontFamily = NewsreaderFamily,
                    fontSize = 17.sp,
                    color = LaterInkPrimary
                )

                Spacer(modifier = Modifier.height(4.dp))

                // 2. Second: Your first 50 memories are free.
                Text(
                    text = "Your first 50 memories are free.",
                    fontFamily = NewsreaderFamily,
                    fontSize = 19.sp,
                    fontWeight = FontWeight.Bold,
                    color = LaterInkPrimary
                )

                Spacer(modifier = Modifier.height(4.dp))

                // 3. Third: Pro removes the limit.
                Text(
                    text = "Pro removes the limit.",
                    fontFamily = NewsreaderFamily,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    color = LaterTerracotta
                )

                Spacer(modifier = Modifier.height(26.dp))

                // 4. Main Pro Benefits Card
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(1.dp, RoundedCornerShape(8.dp), ambientColor = LaterInkPrimary.copy(alpha = 0.03f))
                        .clip(RoundedCornerShape(8.dp))
                        .background(LaterCardBg)
                        .border(1.dp, LaterBorder, RoundedCornerShape(8.dp))
                        .padding(24.dp)
                ) {
                    BookmarkCorner(
                        modifier = Modifier.align(Alignment.TopEnd),
                        size = 16.dp,
                        color = LaterTerracotta
                    )

                    Column {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "PRO",
                                fontFamily = PlusJakartaSansFamily,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Black,
                                letterSpacing = 1.6.sp,
                                color = LaterTerracotta
                            )

                            Text(
                                text = if (isPro) "ACTIVE" else "$totalCount / 50 SAVED",
                                fontFamily = PlusJakartaSansFamily,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                letterSpacing = 0.8.sp,
                                color = LaterTextMuted
                            )
                        }

                        Spacer(modifier = Modifier.height(18.dp))

                        val benefits = listOf(
                            "Unlimited memories",
                            "Keep building your archive",
                            "Full WHY-first search",
                            "Your saved context stays yours"
                        )

                        benefits.forEach { benefit ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(vertical = 7.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(20.dp)
                                        .clip(CircleShape)
                                        .background(LaterTerracottaLight),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "✓",
                                        fontFamily = PlusJakartaSansFamily,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = LaterTerracotta
                                    )
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = benefit,
                                    fontFamily = PlusJakartaSansFamily,
                                    fontSize = 14.5.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = LaterInkPrimary
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Philosophical Line: "50 memories is enough to experience LATER. Pro is for keeping it."
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(6.dp))
                        .background(LaterPaperBg)
                        .border(1.dp, LaterBorderSubtle, RoundedCornerShape(6.dp))
                        .padding(horizontal = 16.dp, vertical = 14.dp)
                ) {
                    Text(
                        text = "“50 memories is enough to experience LATER.\nPro is for keeping it.”",
                        fontFamily = NewsreaderFamily,
                        fontSize = 15.sp,
                        lineHeight = 22.sp,
                        fontWeight = FontWeight.Medium,
                        color = LaterWarmGray,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Spacer(modifier = Modifier.height(28.dp))

                // 5. Purchase Action: [ CONTINUE WITH PRO ] (Terracotta accent)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(6.dp))
                        .background(if (isPro) LaterInkPrimary else LaterTerracotta)
                        .clickable {
                            if (isPro) {
                                Toast.makeText(context, "You are already a PRO subscriber.", Toast.LENGTH_SHORT).show()
                            } else {
                                viewModel.setProSubscriber(true)
                                Toast.makeText(context, "Welcome to LATER PRO. Your archive is unlimited.", Toast.LENGTH_LONG).show()
                            }
                            onNavigateBack()
                        }
                        .padding(vertical = 16.dp)
                        .testTag("paywall_subscribe_button"),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (isPro) "PRO ACTIVE — RETURN TO ARCHIVE" else "CONTINUE WITH PRO",
                        fontFamily = PlusJakartaSansFamily,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.2.sp,
                        color = Color.White
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                // 6. Restore purchases
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            viewModel.setProSubscriber(true)
                            Toast.makeText(context, "Purchases restored successfully.", Toast.LENGTH_SHORT).show()
                        }
                        .padding(vertical = 10.dp)
                        .testTag("paywall_restore_button"),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Restore purchases",
                        fontFamily = PlusJakartaSansFamily,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = LaterTextMuted
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                // Unobtrusive "Not now" dismissal
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onNavigateBack() }
                        .padding(vertical = 10.dp)
                        .testTag("paywall_not_now_button"),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Not now",
                        fontFamily = PlusJakartaSansFamily,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Normal,
                        color = LaterWarmGray
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "Subscription is managed securely via Google Play.\nExisting memories and search always remain accessible.",
                    fontFamily = PlusJakartaSansFamily,
                    fontSize = 11.sp,
                    lineHeight = 16.sp,
                    color = LaterTextMuted,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}
