package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.components.DailyFocusCard
import com.example.ui.components.FocusSessionDialog
import com.example.ui.components.LaterLogoMark
import com.example.ui.components.MemoryCard
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
import com.example.ui.theme.LaterTypographyTokens
import com.example.ui.theme.LaterWarmGray
import com.example.ui.theme.NewsreaderFamily
import com.example.ui.theme.PlusJakartaSansFamily
import com.example.viewmodel.MemorySortOption
import com.example.viewmodel.MemoryViewModel

@Composable
fun HomeScreen(
    viewModel: MemoryViewModel,
    onNavigateToCapture: () -> Unit,
    onNavigateToSearch: () -> Unit,
    onNavigateToDetail: (String) -> Unit,
    onNavigateToPaywall: () -> Unit
) {
    val haptic = LocalHapticFeedback.current
    val memories by viewModel.homeMemories.collectAsStateWithLifecycle()
    val totalCount by viewModel.memoryCount.collectAsStateWithLifecycle()
    val isPro by viewModel.isProSubscriber.collectAsStateWithLifecycle()
    val selectedCategory by viewModel.selectedCategory.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val sortOption by viewModel.sortOption.collectAsStateWithLifecycle()

    var showProfileDialog by remember { mutableStateOf(false) }
    var showSortMenu by remember { mutableStateOf(false) }
    var showFocusDialog by remember { mutableStateOf(false) }

    val categories = listOf("All", "Learn", "Buy", "Try", "Reference", "Idea")

    val searchInteractionSource = remember { MutableInteractionSource() }
    val isSearchFocused by searchInteractionSource.collectIsFocusedAsState()

    val fabInteractionSource = remember { MutableInteractionSource() }
    val isFabPressed by fabInteractionSource.collectIsPressedAsState()
    val fabScale by animateFloatAsState(
        targetValue = if (isFabPressed) 0.94f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMediumLow
        ),
        label = "fab_scale"
    )

    Scaffold(
        containerColor = LaterPaperBg,
        floatingActionButton = {
            // 6. Floating action: Rust-orange circular plus button in bottom-right with "Save memory" pill label
            ExtendedFloatingActionButton(
                onClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    onNavigateToCapture()
                },
                interactionSource = fabInteractionSource,
                containerColor = LaterTerracotta,
                contentColor = Color.White,
                shape = RoundedCornerShape(28.dp),
                icon = {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Save Memory Icon",
                        modifier = Modifier.size(20.dp)
                    )
                },
                text = {
                    Text(
                        text = "Save memory",
                        fontFamily = PlusJakartaSansFamily,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.4.sp
                    )
                },
                modifier = Modifier
                    .testTag("home_fab_add")
                    .graphicsLayer {
                        scaleX = fabScale
                        scaleY = fabScale
                    }
                    .shadow(
                        elevation = 6.dp,
                        shape = RoundedCornerShape(28.dp),
                        ambientColor = LaterTerracotta.copy(alpha = 0.35f),
                        spotColor = LaterDarkAccent.copy(alpha = 0.4f)
                    )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 640.dp)
            ) {
                // =============================================================
                // 1. HEADER SECTION
                // =============================================================
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp, end = 20.dp, top = 16.dp, bottom = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Top-Left: LATER logo with small orange bookmark/document symbol + Subtitle: “WHY FIRST”
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clickable {
                                haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                showProfileDialog = true
                            }
                            .testTag("home_brand_header")
                    ) {
                        LaterLogoMark(
                            size = 28.dp,
                            tint = LaterTerracotta,
                            foldTint = LaterDarkAccent,
                            cutoutColor = Color(0xFFFCFAF5)
                        )

                        Spacer(modifier = Modifier.width(10.dp))

                        Column {
                            Text(
                                text = "LATER",
                                style = LaterTypographyTokens.brandWordmark
                            )
                            Text(
                                text = "WHY FIRST",
                                style = LaterTypographyTokens.brandSecondary,
                                modifier = Modifier.offset(y = (-2).dp)
                            )
                        }
                    }

                    // Top-Right: small circular profile/settings icon and text “3 NEED ATTENTION”
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // Attention status pill: "3 NEED ATTENTION"
                        val attentionCount = if (totalCount > 0) totalCount else 3
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(LaterCardBg)
                                .border(1.dp, LaterBorder, RoundedCornerShape(6.dp))
                                .clickable {
                                    haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                    onNavigateToPaywall()
                                }
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                                .testTag("home_active_memories_badge")
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(6.dp)
                                        .clip(CircleShape)
                                        .background(LaterTerracotta)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "$attentionCount NEED ATTENTION",
                                    style = LaterTypographyTokens.archiveCounter
                                )
                            }
                        }

                        // Small circular profile or settings icon
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(LaterCardBg)
                                .border(1.dp, LaterBorder, CircleShape)
                            .clickable {
                                haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                showProfileDialog = true
                            }
                            .testTag("home_profile_icon_button"),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Person,
                                contentDescription = "Profile and archive settings",
                                tint = LaterInkPrimary,
                                modifier = Modifier.size(19.dp)
                            )
                        }
                    }
                }

                // =============================================================
                // SCROLLABLE CONTENT (Statement, Focus Card, Search, Cards)
                // =============================================================
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .testTag("memories_list"),
                    contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // =========================================================
                    // 2. MAIN PRODUCT STATEMENT
                    // =========================================================
                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 4.dp, bottom = 4.dp)
                        ) {
                            Text(
                                text = "Save the why.",
                                fontFamily = NewsreaderFamily,
                                fontSize = 34.sp,
                                fontWeight = FontWeight.SemiBold,
                                lineHeight = 38.sp,
                                letterSpacing = (-0.5).sp,
                                color = LaterInkPrimary
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Act when it matters.",
                                fontFamily = PlusJakartaSansFamily,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Medium,
                                lineHeight = 22.sp,
                                color = LaterSecondaryText
                            )
                        }
                    }

                    // =========================================================
                    // 3. FEATURED DAILY-FOCUS CARD
                    // =========================================================
                    item {
                        DailyFocusCard(
                            onStartFocusSession = {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                showFocusDialog = true
                            }
                        )
                    }

                    // =========================================================
                    // 4. SEARCH AND FILTER AREA
                    // =========================================================
                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 4.dp)
                        ) {
                            // Rounded search field with search icon
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .shadow(1.dp, RoundedCornerShape(12.dp), ambientColor = LaterInkPrimary.copy(alpha = 0.03f))
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(LaterCardBg)
                                    .border(
                                        width = 1.dp,
                                        color = if (isSearchFocused || searchQuery.isNotEmpty()) LaterTerracotta.copy(alpha = 0.7f) else LaterBorder,
                                        shape = RoundedCornerShape(12.dp)
                                    )
                                    .padding(horizontal = 14.dp, vertical = 12.dp)
                                    .testTag("home_search_bar")
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Search,
                                        contentDescription = "Search archive",
                                        tint = if (searchQuery.isNotEmpty() || isSearchFocused) LaterTerracotta else LaterWarmGray,
                                        modifier = Modifier.size(20.dp)
                                    )

                                    Spacer(modifier = Modifier.width(10.dp))

                                    Box(modifier = Modifier.weight(1f)) {
                                        if (searchQuery.isEmpty()) {
                                            Text(
                                                text = "Search by why, title, or source",
                                                style = LaterTypographyTokens.searchPlaceholder
                                            )
                                        }
                                        BasicTextField(
                                            value = searchQuery,
                                            onValueChange = { viewModel.setSearchQuery(it) },
                                            interactionSource = searchInteractionSource,
                                            textStyle = LaterTypographyTokens.searchInput,
                                            singleLine = true,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .testTag("home_search_input")
                                        )
                                    }

                                    if (searchQuery.isNotEmpty()) {
                                        IconButton(
                                            onClick = {
                                                haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                                viewModel.clearSearch()
                                            },
                                            modifier = Modifier
                                                .size(24.dp)
                                                .testTag("home_clear_search_btn")
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Clear,
                                                contentDescription = "Clear search query",
                                                tint = LaterWarmGray,
                                                modifier = Modifier.size(16.dp)
                                            )
                                        }
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            // Horizontally scrollable filter tabs: All, Learn, Buy, Try, Reference, Idea & sort icon
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    modifier = Modifier
                                        .weight(1f)
                                        .horizontalScroll(rememberScrollState()),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    categories.forEach { category ->
                                        val isSelected = if (category == "All") {
                                            selectedCategory == null
                                        } else {
                                            selectedCategory.equals(category, ignoreCase = true)
                                        }

                                        val indicatorWidth by animateDpAsState(
                                            targetValue = if (isSelected) 22.dp else 0.dp,
                                            animationSpec = spring(
                                                dampingRatio = Spring.DampingRatioNoBouncy,
                                                stiffness = Spring.StiffnessMedium
                                            ),
                                            label = "cat_indicator_$category"
                                        )

                                        Column(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(6.dp))
                                                .background(if (isSelected) LaterTerracottaLight else Color.Transparent)
                                                .border(
                                                    width = 1.dp,
                                                    color = if (isSelected) LaterTerracotta.copy(alpha = 0.35f) else Color.Transparent,
                                                    shape = RoundedCornerShape(6.dp)
                                                )
                                                .clickable {
                                                    haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                                    if (category == "All") {
                                                        viewModel.selectCategory(null)
                                                    } else {
                                                        viewModel.selectCategory(category)
                                                    }
                                                }
                                                .padding(horizontal = 12.dp, vertical = 7.dp)
                                                .testTag("category_tab_$category"),
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {
                                            Text(
                                                text = category,
                                                style = LaterTypographyTokens.categoryTab(isSelected)
                                            )
                                            if (isSelected) {
                                                Spacer(modifier = Modifier.height(3.dp))
                                                Box(
                                                    modifier = Modifier
                                                        .width(indicatorWidth)
                                                        .height(2.dp)
                                                        .clip(RoundedCornerShape(1.dp))
                                                        .background(LaterTerracotta)
                                                )
                                            }
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.width(8.dp))

                                // Compact sort/filter icon on the right
                                Box {
                                    Box(
                                        modifier = Modifier
                                            .size(36.dp)
                                            .clip(RoundedCornerShape(6.dp))
                                            .background(LaterCardBg)
                                            .border(1.dp, LaterBorder, RoundedCornerShape(6.dp))
                                            .clickable {
                                                haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                                showSortMenu = true
                                            }
                                            .testTag("home_sort_icon_button"),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.FilterList,
                                            contentDescription = "Filter and sort options",
                                            tint = LaterInkPrimary,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }

                                    DropdownMenu(
                                        expanded = showSortMenu,
                                        onDismissRequest = { showSortMenu = false }
                                    ) {
                                        MemorySortOption.entries.forEach { option ->
                                            val isCurrentSort = sortOption == option
                                            DropdownMenuItem(
                                                text = {
                                                    Row(
                                                        modifier = Modifier.fillMaxWidth(),
                                                        horizontalArrangement = Arrangement.SpaceBetween,
                                                        verticalAlignment = Alignment.CenterVertically
                                                    ) {
                                                        Text(
                                                            text = option.label,
                                                            fontFamily = PlusJakartaSansFamily,
                                                            fontSize = 13.5.sp,
                                                            fontWeight = if (isCurrentSort) FontWeight.Bold else FontWeight.Medium,
                                                            color = if (isCurrentSort) LaterTerracotta else LaterInkPrimary
                                                        )
                                                        if (isCurrentSort) {
                                                            Spacer(modifier = Modifier.width(12.dp))
                                                            Icon(
                                                                imageVector = Icons.Default.Check,
                                                                contentDescription = null,
                                                                tint = LaterTerracotta,
                                                                modifier = Modifier.size(16.dp)
                                                            )
                                                        }
                                                    }
                                                },
                                                onClick = {
                                                    viewModel.setSortOption(option)
                                                    showSortMenu = false
                                                }
                                            )
                                        }
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(6.dp))

                            // Subtle divider
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(1.dp)
                                    .background(LaterBorderSubtle)
                            )
                        }
                    }

                    // =========================================================
                    // 5. MEMORY CARDS
                    // =========================================================
                    if (memories.isEmpty()) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 40.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier.padding(vertical = 16.dp)
                                ) {
                                    LaterLogoMark(
                                        size = 32.dp,
                                        tint = LaterTerracotta,
                                        foldTint = LaterDarkAccent,
                                        cutoutColor = Color(0xFFFCFAF5)
                                    )

                                    Spacer(modifier = Modifier.height(18.dp))

                                    Text(
                                        text = if (searchQuery.isNotBlank()) "NO MATCHING MEMORIES" else "NO SAVED MEMORIES YET",
                                        style = LaterTypographyTokens.emptyHeadline
                                    )

                                    Spacer(modifier = Modifier.height(8.dp))

                                    Text(
                                        text = if (searchQuery.isNotBlank()) {
                                            "No entries match “$searchQuery”. Search by the reason why you saved it."
                                        } else {
                                            "Capture links, notes, or references along with why they matter. Turn them into action later."
                                        },
                                        style = LaterTypographyTokens.emptySubtitle,
                                        textAlign = TextAlign.Center
                                    )

                                    Spacer(modifier = Modifier.height(24.dp))

                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(8.dp))
                                            .border(1.dp, LaterBorder, RoundedCornerShape(8.dp))
                                            .background(LaterCardBg)
                                            .clickable {
                                                haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                                if (searchQuery.isNotBlank()) {
                                                    viewModel.clearSearch()
                                                } else {
                                                    onNavigateToCapture()
                                                }
                                            }
                                            .padding(horizontal = 22.dp, vertical = 12.dp)
                                            .testTag("home_empty_action_button")
                                    ) {
                                        Text(
                                            text = if (searchQuery.isNotBlank()) "CLEAR SEARCH" else "+ SAVE FIRST MEMORY",
                                            fontFamily = PlusJakartaSansFamily,
                                            fontSize = 13.5.sp,
                                            fontWeight = FontWeight.Bold,
                                            letterSpacing = 1.sp,
                                            color = LaterTerracotta
                                        )
                                    }
                                }
                            }
                        }
                    } else {
                        items(memories, key = { it.id }) { memory ->
                            MemoryCard(
                                memory = memory,
                                onClick = { onNavigateToDetail(memory.id) },
                                onDelete = { viewModel.deleteMemory(memory) }
                            )
                        }
                    }

                    // Editorial Colophon
                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            LaterLogoMark(
                                size = 18.dp,
                                tint = LaterTerracotta.copy(alpha = 0.7f),
                                foldTint = LaterDarkAccent.copy(alpha = 0.7f),
                                cutoutColor = Color(0xFFFCFAF5)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "LATER · “SAVE THE WHY. ACT WHEN IT MATTERS.”",
                                fontFamily = PlusJakartaSansFamily,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.ExtraBold,
                                letterSpacing = 1.1.sp,
                                color = LaterSecondaryText
                            )
                        }
                    }

                    // Ensuring Floating Action Button does not cover any card actions
                    item {
                        Spacer(modifier = Modifier.height(96.dp))
                    }
                }
            }
        }
    }

    // Interactive Focus Session Dialog
    if (showFocusDialog) {
        FocusSessionDialog(
            onDismissRequest = { showFocusDialog = false }
        )
    }

    // Profile & Archive Philosophy Dialog
    if (showProfileDialog) {
        AlertDialog(
            onDismissRequest = { showProfileDialog = false },
            containerColor = LaterCardBg,
            shape = RoundedCornerShape(12.dp),
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    LaterLogoMark(
                        size = 24.dp,
                        tint = LaterTerracotta,
                        foldTint = LaterDarkAccent,
                        cutoutColor = Color(0xFFFCFAF5)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "LATER ARCHIVE",
                            fontFamily = PlusJakartaSansFamily,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 1.4.sp,
                            color = LaterInkPrimary
                        )
                        Text(
                            text = "“Save the why. Act when it matters.”",
                            fontFamily = NewsreaderFamily,
                            fontSize = 13.sp,
                            color = LaterTerracotta
                        )
                    }
                }
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(
                        text = "LATER is an intentional memory and saved-resource app. It captures why you saved a link, note, product, tutorial, or reference, then helps you turn that memory into appropriate next action.",
                        fontFamily = NewsreaderFamily,
                        fontSize = 15.5.sp,
                        lineHeight = 23.sp,
                        color = LaterInkPrimary
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(LaterPaperBg)
                            .border(1.dp, LaterBorder, RoundedCornerShape(8.dp))
                            .padding(12.dp)
                    ) {
                        Column {
                            Text(
                                text = "ARCHIVE STATUS",
                                fontFamily = PlusJakartaSansFamily,
                                fontSize = 11.5.sp,
                                fontWeight = FontWeight.ExtraBold,
                                letterSpacing = 1.sp,
                                color = LaterTerracotta
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = if (isPro) "Active Pro Member · Unlimited Archive" else "$totalCount / 50 Active Memories",
                                fontFamily = PlusJakartaSansFamily,
                                fontSize = 13.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = LaterInkPrimary
                            )
                            Text(
                                text = "Today: turn one saved idea into progress.",
                                fontFamily = PlusJakartaSansFamily,
                                fontSize = 12.5.sp,
                                fontWeight = FontWeight.Medium,
                                color = LaterSecondaryText
                            )
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    showProfileDialog = false
                    onNavigateToPaywall()
                }) {
                    Text(
                        text = if (isPro) "Membership Details" else "Upgrade to Pro",
                        fontFamily = PlusJakartaSansFamily,
                        fontWeight = FontWeight.Bold,
                        color = LaterTerracotta
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = { showProfileDialog = false }) {
                    Text(
                        text = "Close",
                        fontFamily = PlusJakartaSansFamily,
                        fontWeight = FontWeight.SemiBold,
                        color = LaterInkPrimary
                    )
                }
            }
        )
    }
}
