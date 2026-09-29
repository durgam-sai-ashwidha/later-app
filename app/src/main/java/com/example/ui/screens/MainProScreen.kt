package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.MemoryEntity
import com.example.ui.components.AddMemoryBottomSheet
import com.example.ui.components.LaterNavBar
import com.example.ui.components.LaterNavTab
import com.example.ui.components.ProfileSettingsDialog
import com.example.ui.components.WelcomeOnboardingDialog
import com.example.ui.theme.LaterDarkAccent
import com.example.ui.theme.LaterPaperBg
import com.example.ui.theme.LaterTerracotta
import com.example.ui.theme.PlusJakartaSansFamily
import com.example.viewmodel.MemoryViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainProScreen(
    viewModel: MemoryViewModel,
    onNavigateToDetail: (String) -> Unit,
    onNavigateToFocus: (String?) -> Unit,
    onNavigateToSearch: () -> Unit,
    onNavigateToPaywall: () -> Unit
) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val haptic = LocalHapticFeedback.current
    val coroutineScope = rememberCoroutineScope()

    var currentTab by remember { mutableStateOf(LaterNavTab.TODAY) }
    var showAddSheet by remember { mutableStateOf(false) }
    var showProfileDialog by remember { mutableStateOf(false) }

    val addSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val memoryCount by viewModel.memoryCount.collectAsStateWithLifecycle()
    val streak by viewModel.followThroughStreak.collectAsStateWithLifecycle()
    val focusSessionsCompleted by viewModel.focusSessionsCompleted.collectAsStateWithLifecycle()
    val isDemoMode by viewModel.isDemoMode.collectAsStateWithLifecycle()
    val isPro by viewModel.isProSubscriber.collectAsStateWithLifecycle()
    val activeMomentsCount by viewModel.activeMomentsCount.collectAsStateWithLifecycle()
    val hasSeenWelcome by viewModel.hasSeenWelcome.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val selectedCategory by viewModel.selectedCategory.collectAsStateWithLifecycle()
    val selectedStateFilter by viewModel.selectedStateFilter.collectAsStateWithLifecycle()

    var userMemoryCount by remember { mutableStateOf(0) }
    LaunchedEffect(showProfileDialog) {
        if (showProfileDialog) {
            userMemoryCount = viewModel.getUserMemoryCount()
        }
    }

    // Determine FAB visibility:
    // On Empty Memory Shelf: FAB is hidden, only the centered "SAVE YOUR FIRST WHY" button is shown.
    // On Search / Filter with no results: FAB remains visible.
    // On populated screens: Single universal circular FAB.
    val shouldShowFab = when (currentTab) {
        LaterNavTab.ARCHIVE -> !(memoryCount == 0 && searchQuery.isBlank() && selectedCategory == null && selectedStateFilter == "All")
        LaterNavTab.TODAY -> !(memoryCount == 0 && !isDemoMode)
        LaterNavTab.INSIGHTS -> !(memoryCount == 0 && !isDemoMode)
        LaterNavTab.REMINDERS -> true
    }

    Scaffold(
        containerColor = LaterPaperBg,
        bottomBar = {
            LaterNavBar(
                selectedTab = currentTab,
                onTabSelected = { currentTab = it }
            )
        },
        floatingActionButton = {
            if (shouldShowFab) {
                // Universal rust-orange circular floating action button with plus icon
                FloatingActionButton(
                    onClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        showAddSheet = true
                    },
                    containerColor = LaterTerracotta,
                    contentColor = Color.White,
                    shape = CircleShape,
                    modifier = Modifier
                        .testTag("pro_fab_save_memory")
                        .shadow(
                            elevation = 6.dp,
                            shape = CircleShape,
                            ambientColor = LaterTerracotta.copy(alpha = 0.35f),
                            spotColor = LaterDarkAccent.copy(alpha = 0.4f)
                        )
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Save Memory Icon",
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentTab) {
                LaterNavTab.TODAY -> {
                    TodayScreen(
                        viewModel = viewModel,
                        onStartFocusSession = { memory ->
                            onNavigateToFocus(memory?.id)
                        },
                        onNavigateToDetail = onNavigateToDetail,
                        onOpenProfile = { showProfileDialog = true },
                        onSaveTheWhy = { showAddSheet = true }
                    )
                }

                LaterNavTab.ARCHIVE -> {
                    HomeScreen(
                        viewModel = viewModel,
                        onNavigateToCapture = { showAddSheet = true },
                        onNavigateToSearch = onNavigateToSearch,
                        onNavigateToDetail = onNavigateToDetail,
                        onNavigateToPaywall = onNavigateToPaywall,
                        onStartFocusSession = { memory ->
                            onNavigateToFocus(memory?.id)
                        },
                        onOpenProfile = { showProfileDialog = true }
                    )
                }

                LaterNavTab.REMINDERS -> {
                    RemindersScreen(
                        viewModel = viewModel,
                        onStartFocusSession = { memory ->
                            onNavigateToFocus(memory.id)
                        },
                        onNavigateToDetail = onNavigateToDetail
                    )
                }

                LaterNavTab.INSIGHTS -> {
                    InsightsScreen(
                        viewModel = viewModel,
                        isPro = isPro,
                        onOpenPaywall = onNavigateToPaywall,
                        onFinishReview = {
                            currentTab = LaterNavTab.TODAY
                        },
                        onCreateFirstLaterMoment = {
                            showAddSheet = true
                        }
                    )
                }
            }
        }
    }

    // Add Memory Flow Bottom Sheet (Save the why)
    if (showAddSheet) {
        AddMemoryBottomSheet(
            sheetState = addSheetState,
            isPro = isPro,
            activeMomentsCount = activeMomentsCount,
            onDismissRequest = { showAddSheet = false },
            onOpenPaywall = onNavigateToPaywall,
            onSaveMemory = { title, why, content, category, trigger, tinyAction ->
                viewModel.saveLaterMoment(
                    title = title,
                    why = why,
                    content = content,
                    category = category,
                    trigger = trigger,
                    tinyAction = tinyAction,
                    onSaved = {
                        coroutineScope.launch {
                            addSheetState.hide()
                            showAddSheet = false
                        }
                    }
                )
            }
        )
    }

    // Profile & Settings Dialog (with Demo Workspace toggle & confirmation)
    if (showProfileDialog) {
        ProfileSettingsDialog(
            memoryCount = memoryCount,
            streak = streak,
            focusSessionsCompleted = focusSessionsCompleted,
            isDemoMode = isDemoMode,
            isPro = isPro,
            userMemoryCount = userMemoryCount,
            onLoadDemoWorkspace = { viewModel.loadDemoWorkspace() },
            onClearDemoWorkspace = { viewModel.clearDemoWorkspace() },
            onOpenPaywall = onNavigateToPaywall,
            onRestorePurchases = {
                viewModel.restorePurchases(
                    onSuccess = { hasPro ->
                        if (hasPro) {
                            android.widget.Toast.makeText(
                                context,
                                "LATER Pro is ready. Your future has more room.",
                                android.widget.Toast.LENGTH_LONG
                            ).show()
                        } else {
                            android.widget.Toast.makeText(
                                context,
                                "No previous LATER Pro purchase was found.",
                                android.widget.Toast.LENGTH_LONG
                            ).show()
                        }
                    },
                    onError = {
                        android.widget.Toast.makeText(
                            context,
                            "Purchase didn’t go through. Please try again.",
                            android.widget.Toast.LENGTH_SHORT
                        ).show()
                    }
                )
            },
            onDismissRequest = { showProfileDialog = false }
        )
    }

    // First-launch welcome screen (Fresh User Mode)
    if (!hasSeenWelcome && memoryCount == 0 && !isDemoMode) {
        WelcomeOnboardingDialog(
            onSaveFirstWhy = {
                viewModel.completeWelcome()
                showAddSheet = true
            },
            onDismiss = {
                viewModel.completeWelcome()
            }
        )
    }
}
