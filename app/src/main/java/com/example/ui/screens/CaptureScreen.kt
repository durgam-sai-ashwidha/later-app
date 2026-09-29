package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.components.BookmarkCorner
import com.example.ui.components.WhyCaptureInput
import com.example.ui.theme.LaterBorder
import com.example.ui.theme.LaterBorderSubtle
import com.example.ui.theme.LaterCardBg
import com.example.ui.theme.LaterInkPrimary
import com.example.ui.theme.LaterPaperBg
import com.example.ui.theme.LaterSecondaryText
import com.example.ui.theme.LaterTerracotta
import com.example.ui.theme.LaterWarmGray
import com.example.ui.theme.LaterTextMuted
import com.example.ui.theme.LaterTypographyTokens
import com.example.ui.theme.LaterWhyBg
import com.example.ui.theme.NewsreaderFamily
import com.example.ui.theme.PlusJakartaSansFamily
import com.example.viewmodel.ExtractionState
import com.example.viewmodel.MemoryViewModel

@Composable
fun CaptureScreen(
    viewModel: MemoryViewModel,
    onNavigateBack: () -> Unit,
    onMemorySaved: () -> Unit,
    initialSharedContent: String? = null
) {
    val haptic = LocalHapticFeedback.current
    var contentInput by remember { mutableStateOf(initialSharedContent ?: "") }
    var whyInput by remember { mutableStateOf("") }
    var titleInput by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf<String?>("LEARN") }
    var isReviewed by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isSavedSuccess by remember { mutableStateOf(false) }

    val extractionState by viewModel.extractionState.collectAsStateWithLifecycle()

    // Android Hardware / Gesture Back navigation handling
    BackHandler(enabled = true) {
        if (isReviewed) {
            isReviewed = false
        } else {
            viewModel.resetExtractionState()
            onNavigateBack()
        }
    }

    LaunchedEffect(initialSharedContent) {
        if (!initialSharedContent.isNullOrBlank()) {
            contentInput = initialSharedContent
        }
    }

    // React to AI extraction result
    LaunchedEffect(extractionState) {
        when (val state = extractionState) {
            is ExtractionState.Success -> {
                val result = state.result
                if (titleInput.isBlank()) titleInput = result.title
                if (whyInput.isBlank()) whyInput = result.why
                if (!result.category.isNullOrBlank()) selectedCategory = result.category
                isReviewed = true
                viewModel.resetExtractionState()
            }
            is ExtractionState.Error -> {
                errorMessage = state.message
            }
            else -> {}
        }
    }

    val categories = listOf("LEARN", "BUY", "TRY", "REFERENCE", "IDEA")

    // React to save success
    LaunchedEffect(isSavedSuccess) {
        if (isSavedSuccess) {
            kotlinx.coroutines.delay(850)
            onMemorySaved()
        }
    }

    fun handleSave() {
        if (whyInput.isBlank()) {
            errorMessage = "The reason WHY you saved this is required. Tell future-you why."
            return
        }

        isSavedSuccess = true
        haptic.performHapticFeedback(HapticFeedbackType.LongPress)

        viewModel.saveMemory(
            content = contentInput.ifBlank { whyInput },
            title = titleInput.ifBlank { contentInput.take(60) },
            why = whyInput.trim(),
            category = selectedCategory,
            onSaved = { /* Handled by LaunchedEffect */ }
        )
    }

    Scaffold(
        containerColor = LaterPaperBg
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .imePadding()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 680.dp)
                    .padding(horizontal = 24.dp, vertical = 18.dp)
            ) {
                // Header Bar
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = {
                            if (isReviewed) {
                                isReviewed = false
                            } else {
                                viewModel.resetExtractionState()
                                onNavigateBack()
                            }
                        },
                        modifier = Modifier.testTag("capture_back_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = LaterInkPrimary,
                            modifier = Modifier.size(22.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Column {
                        Text(
                            text = if (isReviewed) "REVIEW YOUR MEMORY" else "CAPTURE MEMORY",
                            fontFamily = PlusJakartaSansFamily,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 1.6.sp,
                            color = LaterInkPrimary
                        )

                        Text(
                            text = if (isReviewed) "Verify what future-you will see." else "Give future-you enough context.",
                            fontFamily = NewsreaderFamily,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = LaterWarmGray,
                            modifier = Modifier.padding(top = 2.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Error Banner if present
                if (errorMessage != null) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(6.dp))
                            .background(LaterCardBg)
                            .border(1.dp, LaterTerracotta, RoundedCornerShape(6.dp))
                            .padding(14.dp)
                    ) {
                        Text(
                            text = errorMessage ?: "",
                            fontFamily = PlusJakartaSansFamily,
                            fontSize = 13.sp,
                            color = LaterTerracotta
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }

                if (!isReviewed) {
                    // ==========================================
                    // STAGE 1: RAW INPUT & INTENT
                    // ==========================================

                    // FIELD 1: What did you find?
                    Text(
                        text = "1. WHAT ARE YOU SAVING?",
                        fontFamily = PlusJakartaSansFamily,
                        fontSize = 13.5.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 1.2.sp,
                        color = LaterInkPrimary
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(6.dp))
                            .background(LaterCardBg)
                            .border(1.dp, LaterBorder, RoundedCornerShape(6.dp))
                            .padding(16.dp)
                    ) {
                        if (contentInput.isEmpty()) {
                            Text(
                                text = "Paste a URL, article link, quote, or note…",
                                fontFamily = PlusJakartaSansFamily,
                                fontSize = 14.5.sp,
                                color = LaterTextMuted
                            )
                        }
                        BasicTextField(
                            value = contentInput,
                            onValueChange = {
                                contentInput = it
                                errorMessage = null
                            },
                            textStyle = TextStyle(
                                fontFamily = PlusJakartaSansFamily,
                                fontSize = 14.5.sp,
                                color = LaterInkPrimary
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(85.dp)
                                .testTag("capture_content_input")
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // FIELD 2: WHY DOES THIS MATTER? (Primary Entry Point & Visual Centerpiece)
                    WhyCaptureInput(
                        value = whyInput,
                        onValueChange = {
                            whyInput = it
                            errorMessage = null
                        },
                        label = "2. WHY DOES THIS MATTER?",
                        subLabel = "— the heart of LATER",
                        placeholder = "“I need this for next sprint's refactor...” or “Read before exam on Friday...”",
                        isPrimaryEntryPoint = true,
                        minHeight = 110.dp,
                        testTag = "capture_why_input"
                    )

                    Spacer(modifier = Modifier.height(22.dp))

                    // FIELD 3: TITLE (Optional initial title)
                    Text(
                        text = "TITLE (OPTIONAL)",
                        fontFamily = PlusJakartaSansFamily,
                        fontSize = 13.5.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 1.2.sp,
                        color = LaterInkPrimary
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(6.dp))
                            .background(LaterCardBg)
                            .border(1.dp, LaterBorder, RoundedCornerShape(6.dp))
                            .padding(16.dp)
                    ) {
                        if (titleInput.isEmpty()) {
                            Text(
                                text = "Descriptive title or headline",
                                fontFamily = PlusJakartaSansFamily,
                                fontSize = 14.5.sp,
                                color = LaterTextMuted
                            )
                        }
                        BasicTextField(
                            value = titleInput,
                            onValueChange = { titleInput = it },
                            textStyle = TextStyle(
                                fontFamily = PlusJakartaSansFamily,
                                fontSize = 14.5.sp,
                                color = LaterInkPrimary
                            ),
                            singleLine = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("capture_title_input")
                        )
                    }

                    Spacer(modifier = Modifier.height(22.dp))

                    // FIELD 4: CATEGORY
                    Text(
                        text = "CATEGORY",
                        fontFamily = PlusJakartaSansFamily,
                        fontSize = 13.5.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 1.2.sp,
                        color = LaterInkPrimary
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        categories.forEach { cat ->
                            val isSelected = selectedCategory.equals(cat, ignoreCase = true)
                            Column(
                                modifier = Modifier
                                    .clickable {
                                        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                        selectedCategory = if (isSelected) null else cat
                                    }
                                    .padding(vertical = 4.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = cat,
                                    fontFamily = PlusJakartaSansFamily,
                                    fontSize = 13.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 0.8.sp,
                                    color = if (isSelected) LaterTerracotta else LaterSecondaryText
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Box(
                                    modifier = Modifier
                                        .width(if (isSelected) 22.dp else 0.dp)
                                        .height(2.dp)
                                        .clip(RoundedCornerShape(1.dp))
                                        .background(if (isSelected) LaterTerracotta else Color.Transparent)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(30.dp))

                    // AI Suggest Action
                    val isExtracting = extractionState is ExtractionState.Loading

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(6.dp))
                            .background(LaterInkPrimary)
                            .clickable(enabled = !isExtracting) {
                                if (contentInput.isNotBlank() || whyInput.isNotBlank()) {
                                    haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                    viewModel.extractWithAi(
                                        rawContent = contentInput.ifBlank { whyInput }
                                    )
                                } else {
                                    errorMessage = "Please enter content or your reason why first."
                                }
                            }
                            .padding(vertical = 16.dp)
                            .testTag("btn_suggest_ai"),
                        contentAlignment = Alignment.Center
                    ) {
                        if (isExtracting) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                CircularProgressIndicator(
                                    color = LaterCardBg,
                                    modifier = Modifier.size(18.dp),
                                    strokeWidth = 2.dp
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = "Understanding what you saved…",
                                    fontFamily = PlusJakartaSansFamily,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = LaterCardBg
                                )
                            }
                        } else {
                            Text(
                                text = "Suggest with AI →",
                                fontFamily = PlusJakartaSansFamily,
                                fontSize = 14.5.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.5.sp,
                                color = LaterCardBg
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Secondary action: Save without AI
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                handleSave()
                            }
                            .padding(vertical = 12.dp)
                            .testTag("btn_save_without_ai"),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Save directly without AI",
                            fontFamily = PlusJakartaSansFamily,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = LaterWarmGray
                        )
                    }
                } else {
                    // ==========================================
                    // REVIEW STAGE (User Owns the Memory)
                    // ==========================================
                    WhyCaptureInput(
                        value = whyInput,
                        onValueChange = { whyInput = it },
                        label = "WHY YOU SAVED THIS",
                        subLabel = "— editable",
                        isPrimaryEntryPoint = true,
                        minHeight = 120.dp,
                        testTag = "review_why_input"
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Text(
                        text = "TITLE",
                        fontFamily = PlusJakartaSansFamily,
                        fontSize = 13.5.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 1.2.sp,
                        color = LaterInkPrimary
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(6.dp))
                            .background(LaterCardBg)
                            .border(1.dp, LaterBorder, RoundedCornerShape(6.dp))
                            .padding(16.dp)
                    ) {
                        BasicTextField(
                            value = titleInput,
                            onValueChange = { titleInput = it },
                            textStyle = TextStyle(
                                fontFamily = PlusJakartaSansFamily,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = LaterInkPrimary
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("review_title_input")
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Text(
                        text = "CATEGORY",
                        fontFamily = PlusJakartaSansFamily,
                        fontSize = 13.5.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 1.2.sp,
                        color = LaterInkPrimary
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        categories.forEach { cat ->
                            val isSelected = selectedCategory.equals(cat, ignoreCase = true)
                            Column(
                                modifier = Modifier
                                    .clickable {
                                        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                        selectedCategory = if (isSelected) null else cat
                                    }
                                    .padding(vertical = 4.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = cat,
                                    fontFamily = PlusJakartaSansFamily,
                                    fontSize = 13.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 0.8.sp,
                                    color = if (isSelected) LaterTerracotta else LaterSecondaryText
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Box(
                                    modifier = Modifier
                                        .width(if (isSelected) 22.dp else 0.dp)
                                        .height(2.dp)
                                        .clip(RoundedCornerShape(1.dp))
                                        .background(if (isSelected) LaterTerracotta else Color.Transparent)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    // 8. Primary Save Action with subtle tactile success state
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(6.dp))
                            .background(if (isSavedSuccess) LaterInkPrimary else LaterTerracotta)
                            .clickable(enabled = !isSavedSuccess) { handleSave() }
                            .padding(vertical = 16.dp)
                            .testTag("btn_save_memory"),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (isSavedSuccess) "✓ SAVED TO ARCHIVE" else "SAVE MEMORY",
                            fontFamily = PlusJakartaSansFamily,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.2.sp,
                            color = Color.White
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                isReviewed = false
                            }
                            .padding(vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "← Back to edit raw input",
                            fontFamily = PlusJakartaSansFamily,
                            fontSize = 13.5.sp,
                            fontWeight = FontWeight.Medium,
                            color = LaterWarmGray
                        )
                    }
                }
            }
        }

        // 13. SUCCESS STATE: Serene confirmation before returning to archive
        if (isSavedSuccess) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(LaterPaperBg.copy(alpha = 0.96f)),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(32.dp)
                ) {
                    BookmarkCorner(
                        size = 28.dp,
                        color = LaterTerracotta
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Text(
                        text = "MEMORY SAVED",
                        fontFamily = PlusJakartaSansFamily,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 2.sp,
                        color = LaterInkPrimary
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Future-you will understand.",
                        fontFamily = NewsreaderFamily,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        color = LaterWarmGray
                    )
                }
            }
        }
    }
}
