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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.FloatingActionButton
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.components.BookmarkCorner
import com.example.ui.components.LaterLogoMark
import com.example.ui.components.LaterPrimaryLogo
import com.example.ui.components.MemoryCard
import com.example.ui.theme.LaterBorder
import com.example.ui.theme.LaterBorderSubtle
import com.example.ui.theme.LaterCardBg
import com.example.ui.theme.LaterDarkAccent
import com.example.ui.theme.LaterInkPrimary
import com.example.ui.theme.LaterPaperBg
import com.example.ui.theme.LaterTerracotta
import com.example.ui.theme.LaterTerracottaLight
import com.example.ui.theme.LaterWarmGray
import com.example.ui.theme.LaterTextMuted
import com.example.ui.theme.LaterTypographyTokens
import com.example.ui.theme.NewsreaderFamily
import com.example.ui.theme.PlusJakartaSansFamily
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
    var showBrandStoryDialog by remember { mutableStateOf(false) }

    val categories = listOf("ALL", "LEARN", "BUY", "TRY", "REFERENCE", "IDEA")

    // Tactile FAB interaction
    val fabInteractionSource = remember { MutableInteractionSource() }
    val isFabPressed by fabInteractionSource.collectIsPressedAsState()
    val fabScale by animateFloatAsState(
        targetValue = if (isFabPressed) 0.92f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMediumLow
        ),
        label = "fab_scale"
    )

    // Search bar focus tracking
    val searchInteractionSource = remember { MutableInteractionSource() }
    val isSearchFocused by searchInteractionSource.collectIsFocusedAsState()

    Scaffold(
        containerColor = LaterPaperBg,
        floatingActionButton = {
            // Tactile terracotta floating button with subtle haptic response
            FloatingActionButton(
                onClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    onNavigateToCapture()
                },
                interactionSource = fabInteractionSource,
                containerColor = LaterTerracotta,
                contentColor = Color.White,
                shape = CircleShape,
                modifier = Modifier
                    .testTag("home_fab_add")
                    .graphicsLayer {
                        scaleX = fabScale
                        scaleY = fabScale
                    }
                    .shadow(elevation = 5.dp, shape = CircleShape, ambientColor = LaterTerracotta.copy(alpha = 0.3f))
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Save Memory",
                    modifier = Modifier.size(26.dp)
                )
            }
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
                    .widthIn(max = 680.dp)
            ) {
                // Editorial Header Section
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 24.dp, end = 24.dp, top = 22.dp, bottom = 16.dp)
                ) {
                    // Top row: Distinctive Brand Logo System & Quiet Archive Counter
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        LaterPrimaryLogo(
                            markSize = 26.dp,
                            modifier = Modifier
                                .clickable { showBrandStoryDialog = true }
                                .testTag("home_brand_logo")
                        )

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            // Subtle "Why LATER exists" story button
                            IconButton(
                                onClick = {
                                    haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                    showBrandStoryDialog = true
                                },
                                modifier = Modifier
                                    .size(28.dp)
                                    .testTag("home_story_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Info,
                                    contentDescription = "Why LATER exists",
                                    tint = LaterWarmGray,
                                    modifier = Modifier.size(17.dp)
                                )
                            }

                            // 6. Free Memory Indicator: Quiet archive counter, not a commercial banner
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(LaterCardBg)
                                    .border(1.dp, LaterBorderSubtle, RoundedCornerShape(4.dp))
                                    .clickable {
                                        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                        onNavigateToPaywall()
                                    }
                                    .padding(vertical = 5.dp, horizontal = 10.dp)
                                    .testTag("pro_badge_button")
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .size(6.dp)
                                            .clip(CircleShape)
                                            .background(LaterTerracotta)
                                    )
                                    Spacer(modifier = Modifier.width(7.dp))
                                    Text(
                                        text = if (isPro) "PRO ARCHIVE" else "$totalCount / 50 MEMORIES",
                                        style = LaterTypographyTokens.archiveCounter
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Editorial Tagline in Newsreader Serif (token-enforced)
                    Text(
                        text = "Never ask “Why did I save this?” again.",
                        style = LaterTypographyTokens.editorialTagline
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // 4. Archive Search: Thin border, warm background, restrained 6dp corners, clear placeholder
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .shadow(1.dp, RoundedCornerShape(6.dp), ambientColor = LaterInkPrimary.copy(alpha = 0.03f))
                            .clip(RoundedCornerShape(6.dp))
                            .background(LaterCardBg)
                            .border(
                                width = 1.dp,
                                color = if (isSearchFocused || searchQuery.isNotEmpty()) LaterTerracotta.copy(alpha = 0.65f) else LaterBorder,
                                shape = RoundedCornerShape(6.dp)
                            )
                            .padding(horizontal = 14.dp, vertical = 11.dp)
                            .testTag("home_search_bar")
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Search Archive",
                                tint = if (searchQuery.isNotEmpty() || isSearchFocused) LaterTerracotta else LaterWarmGray,
                                modifier = Modifier.size(19.dp)
                            )

                            Spacer(modifier = Modifier.width(10.dp))

                            Box(modifier = Modifier.weight(1f)) {
                                if (searchQuery.isEmpty()) {
                                    Text(
                                        text = "Filter archive by why or title...",
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
                                        .testTag("home_clear_search_button")
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Clear,
                                        contentDescription = "Clear search",
                                        tint = LaterWarmGray,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        }
                    }

                    // Active Archive Filter Status Bar
                    AnimatedVisibility(
                        visible = searchQuery.isNotBlank(),
                        enter = fadeIn(),
                        exit = fadeOut()
                    ) {
                        Column {
                            Spacer(modifier = Modifier.height(10.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(3.dp))
                                            .background(LaterTerracottaLight)
                                            .border(1.dp, LaterTerracotta.copy(alpha = 0.25f), RoundedCornerShape(3.dp))
                                            .padding(horizontal = 7.dp, vertical = 2.dp)
                                    ) {
                                        Text(
                                            text = "INDEX MATCH",
                                            fontFamily = PlusJakartaSansFamily,
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold,
                                            letterSpacing = 0.8.sp,
                                            color = LaterTerracotta
                                        )
                                    }

                                    Text(
                                        text = "${memories.size} ${if (memories.size == 1) "entry" else "entries"} for “$searchQuery”",
                                        fontFamily = PlusJakartaSansFamily,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = LaterInkPrimary
                                    )
                                }

                                Text(
                                    text = "Reset",
                                    fontFamily = PlusJakartaSansFamily,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = LaterTerracotta,
                                    modifier = Modifier
                                        .clickable {
                                            haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                            viewModel.clearSearch()
                                        }
                                        .padding(4.dp)
                                        .testTag("home_reset_search_button")
                                )
                            }
                        }
                    }
                }

                // 5. Category Navigation: Understated typography, terracotta accent, thin underline animation
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState())
                        .padding(horizontal = 24.dp),
                    horizontalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    categories.forEach { category ->
                        val isSelected = if (category == "ALL") {
                            selectedCategory == null
                        } else {
                            selectedCategory.equals(category, ignoreCase = true)
                        }

                        // Subtle underline indicator width animation
                        val indicatorWidth by animateDpAsState(
                            targetValue = if (isSelected) 28.dp else 0.dp,
                            animationSpec = spring(
                                dampingRatio = Spring.DampingRatioNoBouncy,
                                stiffness = Spring.StiffnessMedium
                            ),
                            label = "cat_indicator_$category"
                        )

                        Column(
                            modifier = Modifier
                                .clickable {
                                    haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                    if (category == "ALL") {
                                        viewModel.selectCategory(null)
                                    } else {
                                        viewModel.selectCategory(category)
                                    }
                                }
                                .padding(bottom = 8.dp)
                                .testTag("category_tab_$category"),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = category,
                                style = LaterTypographyTokens.categoryTab(isSelected)
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            // Underline indicator
                            Box(
                                modifier = Modifier
                                    .width(indicatorWidth)
                                    .height(2.dp)
                                    .clip(RoundedCornerShape(1.dp))
                                    .background(if (isSelected) LaterTerracotta else Color.Transparent)
                            )
                        }
                    }
                }

                // Hairline separator below category navigation
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(LaterBorderSubtle)
                )

                // 9. Spacing & vertical rhythm: Calm whitespace, 18dp spacing between archive cards
                if (memories.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 36.dp, vertical = 64.dp),
                        contentAlignment = Alignment.TopCenter
                    ) {
                        if (searchQuery.isNotBlank()) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "NO MATCHING MEMORIES",
                                    fontFamily = PlusJakartaSansFamily,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 1.4.sp,
                                    color = LaterInkPrimary
                                )

                                Spacer(modifier = Modifier.height(10.dp))

                                Text(
                                    text = "No entries match “$searchQuery” in their WHY or title.\nTry searching by the intent or reason you saved it.",
                                    fontFamily = NewsreaderFamily,
                                    fontSize = 16.sp,
                                    lineHeight = 24.sp,
                                    color = LaterWarmGray,
                                    textAlign = TextAlign.Center
                                )

                                Spacer(modifier = Modifier.height(22.dp))

                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .border(1.dp, LaterBorder, RoundedCornerShape(6.dp))
                                        .background(LaterCardBg)
                                        .clickable {
                                            haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                            viewModel.clearSearch()
                                        }
                                        .padding(horizontal = 18.dp, vertical = 10.dp)
                                        .testTag("home_clear_filter_empty_state")
                                ) {
                                    Text(
                                        text = "Clear Search Filter",
                                        fontFamily = PlusJakartaSansFamily,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = LaterTerracotta
                                    )
                                }
                            }
                        } else {
                            // 12. Empty State: Quiet, memorable, with tiny LATER bookmark motif
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.padding(vertical = 36.dp)
                            ) {
                                LaterLogoMark(
                                    size = 28.dp,
                                    tint = LaterTerracotta,
                                    foldTint = LaterDarkAccent,
                                    cutoutColor = Color(0xFFFCFAF5)
                                )

                                Spacer(modifier = Modifier.height(18.dp))

                                Text(
                                    text = "NOTHING HERE YET.",
                                    style = LaterTypographyTokens.emptyHeadline
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                Text(
                                    text = "Future-you hasn't saved anything here.",
                                    style = LaterTypographyTokens.emptySubtitle,
                                    textAlign = TextAlign.Center
                                )

                                Spacer(modifier = Modifier.height(24.dp))

                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .border(1.dp, LaterBorder, RoundedCornerShape(6.dp))
                                        .background(LaterCardBg)
                                        .clickable(onClick = {
                                            haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                            onNavigateToCapture()
                                        })
                                        .padding(horizontal = 22.dp, vertical = 12.dp)
                                        .testTag("home_empty_save_button")
                                ) {
                                    Text(
                                        text = "+ SAVE SOMETHING",
                                        fontFamily = PlusJakartaSansFamily,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        letterSpacing = 1.2.sp,
                                        color = LaterTerracotta
                                    )
                                }
                            }
                        }
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .testTag("memories_list"),
                        contentPadding = PaddingValues(horizontal = 22.dp, vertical = 20.dp),
                        verticalArrangement = Arrangement.spacedBy(20.dp)
                    ) {
                        items(memories, key = { it.id }) { memory ->
                            MemoryCard(
                                memory = memory,
                                onClick = { onNavigateToDetail(memory.id) },
                                onDelete = { viewModel.deleteMemory(memory) }
                            )
                        }
                        item {
                            // Quiet archival colophon
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 32.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                LaterLogoMark(
                                    size = 18.dp,
                                    tint = LaterTerracotta.copy(alpha = 0.6f),
                                    foldTint = LaterDarkAccent.copy(alpha = 0.6f),
                                    cutoutColor = Color(0xFFFCFAF5)
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "LATER · PERSONAL ARCHIVE",
                                    fontFamily = PlusJakartaSansFamily,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 1.4.sp,
                                    color = LaterTextMuted
                                )
                            }
                        }
                        item {
                            Spacer(modifier = Modifier.height(64.dp))
                        }
                    }
                }
            }
        }
    }

    // 4. BRAND STORY: Why LATER exists
    if (showBrandStoryDialog) {
        AlertDialog(
            onDismissRequest = { showBrandStoryDialog = false },
            containerColor = LaterCardBg,
            shape = RoundedCornerShape(8.dp),
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    LaterLogoMark(
                        size = 20.dp,
                        tint = LaterTerracotta,
                        foldTint = LaterDarkAccent,
                        cutoutColor = Color(0xFFFCFAF5)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "WHY LATER EXISTS",
                        fontFamily = PlusJakartaSansFamily,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 1.4.sp,
                        color = LaterInkPrimary
                    )
                }
            },
            text = {
                Column {
                    Text(
                        text = "You saved it for a reason.\n\nMaybe you wanted to learn something.\nMaybe you wanted to buy something.\nMaybe you needed it for a project.\nMaybe you simply didn't want to forget it.\n\nLater, the link is still there.\n\nThe reason isn't.\n\nLATER keeps both.",
                        fontFamily = NewsreaderFamily,
                        fontSize = 16.sp,
                        lineHeight = 24.sp,
                        color = LaterInkPrimary
                    )
                    Spacer(modifier = Modifier.height(18.dp))
                    Text(
                        text = "LATER",
                        fontFamily = PlusJakartaSansFamily,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 1.6.sp,
                        color = LaterInkPrimary
                    )
                    Text(
                        text = "“Keep the reason.”",
                        fontFamily = NewsreaderFamily,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = LaterTerracotta
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = { showBrandStoryDialog = false }) {
                    Text(
                        text = "Understood",
                        fontFamily = PlusJakartaSansFamily,
                        fontWeight = FontWeight.Bold,
                        color = LaterInkPrimary
                    )
                }
            }
        )
    }
}
