package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmarks
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Today
import androidx.compose.material.icons.outlined.Bookmarks
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material.icons.outlined.Today
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.LaterBorderSubtle
import com.example.ui.theme.LaterPaperBg
import com.example.ui.theme.LaterTerracotta
import com.example.ui.theme.LaterTextMuted
import com.example.ui.theme.PlusJakartaSansFamily

enum class LaterNavTab(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val testTag: String
) {
    TODAY(
        title = "Now",
        selectedIcon = Icons.Filled.Today,
        unselectedIcon = Icons.Outlined.Today,
        testTag = "nav_tab_now"
    ),
    ARCHIVE(
        title = "Memory Shelf",
        selectedIcon = Icons.Filled.Bookmarks,
        unselectedIcon = Icons.Outlined.Bookmarks,
        testTag = "nav_tab_shelf"
    ),
    REMINDERS(
        title = "Moments",
        selectedIcon = Icons.Filled.Schedule,
        unselectedIcon = Icons.Outlined.Schedule,
        testTag = "nav_tab_moments"
    ),
    INSIGHTS(
        title = "Proof",
        selectedIcon = Icons.Filled.CheckCircle,
        unselectedIcon = Icons.Outlined.CheckCircle,
        testTag = "nav_tab_proof"
    )
}

@Composable
fun LaterNavBar(
    selectedTab: LaterNavTab,
    onTabSelected: (LaterNavTab) -> Unit,
    modifier: Modifier = Modifier
) {
    val haptic = LocalHapticFeedback.current

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(LaterPaperBg)
            .border(width = 1.dp, color = LaterBorderSubtle)
            .navigationBarsPadding()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            LaterNavTab.entries.forEach { tab ->
                val isSelected = tab == selectedTab

                val contentColorAnimated by animateColorAsState(
                    targetValue = if (isSelected) LaterTerracotta else LaterTextMuted,
                    animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
                    label = "tab_color_${tab.name}"
                )

                Column(
                    modifier = Modifier
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = {
                                if (!isSelected) {
                                    haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                    onTabSelected(tab)
                                }
                            }
                        )
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                        .testTag(tab.testTag),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = if (isSelected) tab.selectedIcon else tab.unselectedIcon,
                        contentDescription = tab.title,
                        tint = contentColorAnimated,
                        modifier = Modifier.size(22.dp)
                    )

                    Spacer(modifier = Modifier.height(3.dp))

                    Text(
                        text = tab.title,
                        fontFamily = PlusJakartaSansFamily,
                        fontSize = 11.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        letterSpacing = 0.3.sp,
                        color = contentColorAnimated
                    )

                    Spacer(modifier = Modifier.height(3.dp))

                    // 2-3 px rust-orange active indicator
                    Box(
                        modifier = Modifier
                            .height(2.5.dp)
                            .width(18.dp)
                            .clip(RoundedCornerShape(1.5.dp))
                            .background(if (isSelected) LaterTerracotta else Color.Transparent)
                    )
                }
            }
        }
    }
}
