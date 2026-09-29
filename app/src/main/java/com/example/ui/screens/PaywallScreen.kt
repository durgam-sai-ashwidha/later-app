package com.example.ui.screens

import android.app.Activity
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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.localizedPriceString
import com.example.ui.components.LaterLogoMark
import com.example.ui.theme.LaterBorder
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
import com.example.viewmodel.MemoryViewModel
import com.revenuecat.purchases.Package
import com.revenuecat.purchases.PackageType

@Composable
fun PaywallScreen(
    viewModel: MemoryViewModel,
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    val activity = context as? Activity

    val offerings by viewModel.currentOfferings.collectAsStateWithLifecycle()

    var isPurchasing by remember { mutableStateOf(false) }
    var isRestoring by remember { mutableStateOf(false) }
    var showTermsDialog by remember { mutableStateOf(false) }
    var showPrivacyDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.refreshCustomerInfo()
    }

    // Identify the monthly package in default offering connected to later_pro_monthly
    val currentOffering = offerings?.current
    val rcPackage: Package? = currentOffering?.monthly
        ?: currentOffering?.availablePackages?.find {
            it.packageType == PackageType.MONTHLY ||
            it.identifier.contains("monthly", ignoreCase = true) ||
            it.product.id == "later_pro_monthly"
        }
        ?: currentOffering?.availablePackages?.firstOrNull()

    // Show actual package price from RevenueCat Package.localizedPriceString
    val priceString = rcPackage?.localizedPriceString ?: "$4.99"

    if (showTermsDialog) {
        AlertDialog(
            onDismissRequest = { showTermsDialog = false },
            containerColor = LaterPaperBg,
            shape = RoundedCornerShape(12.dp),
            title = {
                Text(
                    text = "Terms of Service",
                    fontFamily = PlusJakartaSansFamily,
                    fontWeight = FontWeight.Bold,
                    color = LaterInkPrimary
                )
            },
            text = {
                Text(
                    text = "LATER Pro subscriptions renew automatically unless cancelled at least 24 hours before the end of the current billing cycle through your Google Play account settings. All active Later Moments remain accessible during your active subscription.",
                    fontFamily = PlusJakartaSansFamily,
                    fontSize = 13.sp,
                    lineHeight = 18.sp,
                    color = LaterSecondaryText
                )
            },
            confirmButton = {
                TextButton(onClick = { showTermsDialog = false }) {
                    Text("Close", color = LaterTerracotta)
                }
            }
        )
    }

    if (showPrivacyDialog) {
        AlertDialog(
            onDismissRequest = { showPrivacyDialog = false },
            containerColor = LaterPaperBg,
            shape = RoundedCornerShape(12.dp),
            title = {
                Text(
                    text = "Privacy Policy",
                    fontFamily = PlusJakartaSansFamily,
                    fontWeight = FontWeight.Bold,
                    color = LaterInkPrimary
                )
            },
            text = {
                Text(
                    text = "LATER is offline-first. Your saved reasons, personal reflections, and memory shelf are stored in your local SQLite database. RevenueCat handles secure subscription validation without selling or tracking personal content.",
                    fontFamily = PlusJakartaSansFamily,
                    fontSize = 13.sp,
                    lineHeight = 18.sp,
                    color = LaterSecondaryText
                )
            },
            confirmButton = {
                TextButton(onClick = { showPrivacyDialog = false }) {
                    Text("Close", color = LaterTerracotta)
                }
            }
        )
    }

    Scaffold(
        containerColor = LaterPaperBg
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .navigationBarsPadding()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 600.dp)
                    .padding(horizontal = 24.dp, vertical = 16.dp)
            ) {
                // Top Dismiss Action & Brand
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
                            text = "LATER PRO",
                            fontFamily = PlusJakartaSansFamily,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 1.3.sp,
                            color = LaterTerracotta
                        )
                    }

                    IconButton(
                        onClick = onNavigateBack,
                        modifier = Modifier.testTag("paywall_close_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close paywall",
                            tint = LaterInkPrimary,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Hero Headline: Editorial Serif
                Text(
                    text = "Make every saved intention count.",
                    fontFamily = NewsreaderFamily,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.SemiBold,
                    lineHeight = 36.sp,
                    letterSpacing = (-0.5).sp,
                    color = LaterInkPrimary
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Body text
                Text(
                    text = "Free includes 3 active Later Moments. Pro gives your future unlimited room.",
                    fontFamily = PlusJakartaSansFamily,
                    fontSize = 14.5.sp,
                    lineHeight = 22.sp,
                    color = LaterSecondaryText
                )

                Spacer(modifier = Modifier.height(22.dp))

                // Pro Benefit Card
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(LaterCardBg)
                        .border(1.dp, LaterBorder, RoundedCornerShape(14.dp))
                        .padding(20.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(22.dp)
                                .clip(CircleShape)
                                .background(LaterTerracottaLight),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                                tint = LaterTerracotta,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = "Unlimited active Later Moments",
                            fontFamily = PlusJakartaSansFamily,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = LaterInkPrimary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Package Pricing Card
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(LaterTerracottaLight.copy(alpha = 0.5f))
                        .border(
                            width = 2.dp,
                            color = LaterTerracotta,
                            shape = RoundedCornerShape(12.dp)
                        )
                        .padding(18.dp)
                        .testTag("plan_monthly_card")
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Monthly Subscription",
                                fontFamily = PlusJakartaSansFamily,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = LaterInkPrimary
                            )
                            Spacer(modifier = Modifier.height(3.dp))
                            Text(
                                text = "RevenueCat Test Store · Cancel anytime",
                                fontFamily = PlusJakartaSansFamily,
                                fontSize = 12.sp,
                                color = LaterSecondaryText
                            )
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = priceString,
                                fontFamily = PlusJakartaSansFamily,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = LaterTerracotta
                            )
                            Text(
                                text = "/ month",
                                fontFamily = PlusJakartaSansFamily,
                                fontSize = 11.5.sp,
                                color = LaterTextMuted
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Primary Purchase Action: "UNLOCK LATER PRO"
                Button(
                    onClick = {
                        if (isPurchasing) return@Button
                        isPurchasing = true

                        if (rcPackage != null && activity != null) {
                            viewModel.purchasePackage(
                                activity = activity,
                                rcPackage = rcPackage,
                                onSuccess = {
                                    isPurchasing = false
                                    viewModel.refreshCustomerInfo()
                                    Toast.makeText(
                                        context,
                                        "LATER Pro is ready. Your future has more room.",
                                        Toast.LENGTH_LONG
                                    ).show()
                                    onNavigateBack()
                                },
                                onCancelled = {
                                    isPurchasing = false
                                    Toast.makeText(
                                        context,
                                        "No problem. Your free memories are still here.",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                },
                                onError = {
                                    isPurchasing = false
                                    Toast.makeText(
                                        context,
                                        "Purchase didn’t go through. Please try again.",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            )
                        } else {
                            isPurchasing = false
                            if (!viewModel.isRevenueCatConfigured()) {
                                Toast.makeText(
                                    context,
                                    "RevenueCat Test Store requires a valid REVENUECAT_API_KEY in the AI Studio Secrets panel.",
                                    Toast.LENGTH_LONG
                                ).show()
                            } else {
                                Toast.makeText(
                                    context,
                                    "Purchase didn’t go through. Please try again.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .shadow(4.dp, RoundedCornerShape(10.dp), ambientColor = LaterTerracotta.copy(alpha = 0.35f))
                        .testTag("paywall_purchase_button"),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = LaterTerracotta,
                        contentColor = Color.White
                    ),
                    enabled = !isPurchasing
                ) {
                    if (isPurchasing) {
                        CircularProgressIndicator(
                            color = Color.White,
                            strokeWidth = 2.dp,
                            modifier = Modifier.size(20.dp)
                        )
                    } else {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "UNLOCK LATER PRO",
                                fontFamily = PlusJakartaSansFamily,
                                fontSize = 13.5.sp,
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
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Secondary action: RESTORE PURCHASES
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(
                        onClick = {
                            if (isRestoring) return@TextButton
                            isRestoring = true
                            viewModel.restorePurchases(
                                onSuccess = { hasPro ->
                                    isRestoring = false
                                    if (hasPro) {
                                        Toast.makeText(
                                            context,
                                            "LATER Pro is ready. Your future has more room.",
                                            Toast.LENGTH_LONG
                                        ).show()
                                        onNavigateBack()
                                    } else {
                                        Toast.makeText(
                                            context,
                                            "No previous LATER Pro purchase was found.",
                                            Toast.LENGTH_LONG
                                        ).show()
                                    }
                                },
                                onError = {
                                    isRestoring = false
                                    Toast.makeText(
                                        context,
                                        "Purchase didn’t go through. Please try again.",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            )
                        },
                        modifier = Modifier.testTag("paywall_restore_button"),
                        enabled = !isRestoring
                    ) {
                        if (isRestoring) {
                            CircularProgressIndicator(
                                color = LaterTerracotta,
                                strokeWidth = 1.5.dp,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                        }
                        Text(
                            text = "RESTORE PURCHASES",
                            fontFamily = PlusJakartaSansFamily,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.8.sp,
                            color = LaterTerracotta
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Small actions: "Not now", "Terms", "Privacy"
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Not now",
                        fontFamily = PlusJakartaSansFamily,
                        fontSize = 12.sp,
                        color = LaterTextMuted,
                        modifier = Modifier
                            .clickable { onNavigateBack() }
                            .padding(6.dp)
                    )
                    Text(
                        text = " · ",
                        fontFamily = PlusJakartaSansFamily,
                        fontSize = 12.sp,
                        color = LaterTextMuted
                    )
                    Text(
                        text = "Terms",
                        fontFamily = PlusJakartaSansFamily,
                        fontSize = 12.sp,
                        color = LaterTextMuted,
                        modifier = Modifier
                            .clickable { showTermsDialog = true }
                            .padding(6.dp)
                    )
                    Text(
                        text = " · ",
                        fontFamily = PlusJakartaSansFamily,
                        fontSize = 12.sp,
                        color = LaterTextMuted
                    )
                    Text(
                        text = "Privacy",
                        fontFamily = PlusJakartaSansFamily,
                        fontSize = 12.sp,
                        color = LaterTextMuted,
                        modifier = Modifier
                            .clickable { showPrivacyDialog = true }
                            .padding(6.dp)
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}
