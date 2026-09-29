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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
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
fun ExamplePreviewDialog(
    onDismissRequest: () -> Unit,
    onCreateOwnMemory: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        containerColor = LaterPaperBg,
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .testTag("example_preview_dialog"),
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "EXAMPLE PREVIEW",
                        fontFamily = PlusJakartaSansFamily,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 1.2.sp,
                        color = LaterTerracotta
                    )
                    Text(
                        text = "How a Later Memory works",
                        fontFamily = NewsreaderFamily,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = LaterInkPrimary
                    )
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
            Column(modifier = Modifier.fillMaxWidth()) {
                // Card preview
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(LaterCardBg)
                        .border(1.dp, LaterBorder, RoundedCornerShape(12.dp))
                        .padding(14.dp)
                ) {
                    Column {
                        // Category and status
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "LEARN · READY FOR ACTION",
                                fontFamily = PlusJakartaSansFamily,
                                fontSize = 10.5.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.8.sp,
                                color = LaterTerracotta
                            )
                            Text(
                                text = "ishadeed.com",
                                fontFamily = PlusJakartaSansFamily,
                                fontSize = 11.sp,
                                color = LaterTextMuted
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        // Title
                        Text(
                            text = "Modern CSS Grid & Subgrid Deep Dive",
                            fontFamily = PlusJakartaSansFamily,
                            fontSize = 14.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = LaterInkPrimary
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // Why you saved this
                        Text(
                            text = "WHY YOU SAVED THIS",
                            fontFamily = PlusJakartaSansFamily,
                            fontSize = 9.5.sp,
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 0.8.sp,
                            color = LaterSecondaryText
                        )
                        Spacer(modifier = Modifier.height(3.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(6.dp))
                                .background(LaterWhyBg)
                                .padding(horizontal = 10.dp, vertical = 7.dp)
                        ) {
                            Text(
                                text = "“Need this for next sprint’s dashboard redesign—subgrid may solve the card-alignment issue.”",
                                fontFamily = NewsreaderFamily,
                                fontSize = 13.sp,
                                color = LaterInkPrimary,
                                lineHeight = 18.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Visible Later Moment
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(6.dp))
                                .background(Color(0xFFF4EDE2))
                                .border(1.dp, LaterTerracotta.copy(alpha = 0.22f), RoundedCornerShape(6.dp))
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Column {
                                Text(
                                    text = "LATER MOMENT",
                                    fontFamily = PlusJakartaSansFamily,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    letterSpacing = 1.sp,
                                    color = LaterTerracotta
                                )
                                Text(
                                    text = "WHEN Your dashboard sprint starts today, HELP ME 20-min layout research",
                                    fontFamily = PlusJakartaSansFamily,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = LaterInkPrimary
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Action button
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(6.dp))
                                .background(LaterTerracottaLight)
                                .padding(horizontal = 10.dp, vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "START: REDESIGN DASHBOARD →",
                                fontFamily = PlusJakartaSansFamily,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = LaterTerracotta
                            )
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = null,
                                tint = LaterTerracotta,
                                modifier = Modifier.size(13.dp)
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onDismissRequest()
                    onCreateOwnMemory()
                },
                modifier = Modifier.fillMaxWidth().height(46.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = LaterTerracotta,
                    contentColor = Color.White
                )
            ) {
                Text(
                    text = "CREATE YOUR OWN MEMORY",
                    fontFamily = PlusJakartaSansFamily,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.8.sp
                )
            }
        }
    )
}
