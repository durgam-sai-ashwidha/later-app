package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
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
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
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
import com.example.ui.theme.LaterTypographyTokens
import com.example.ui.theme.LaterWhyBg
import com.example.ui.theme.NewsreaderFamily
import com.example.ui.theme.PlusJakartaSansFamily

enum class AddMemoryMode(val label: String, val icon: androidx.compose.ui.graphics.vector.ImageVector) {
    LINK("Paste a link", Icons.Default.Link),
    NOTE("Add a note", Icons.Default.EditNote),
    PRODUCT("Save product", Icons.Default.ShoppingBag),
    PLACE("Save place", Icons.Default.Place),
    CLIPBOARD("Import from clipboard", Icons.Default.Assignment)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddMemoryBottomSheet(
    sheetState: SheetState,
    isPro: Boolean = false,
    activeMomentsCount: Int = 0,
    onDismissRequest: () -> Unit,
    onOpenPaywall: () -> Unit = {},
    onSaveMemory: (
        title: String,
        why: String,
        content: String,
        category: String,
        trigger: String,
        tinyAction: String
    ) -> Unit
) {
    val haptic = LocalHapticFeedback.current
    val clipboardManager = LocalClipboardManager.current

    var showFreeLimitSheet by remember { mutableStateOf(false) }
    var lockedFeatureForDialog by remember { mutableStateOf<String?>(null) }

    var selectedMode by remember { mutableStateOf(AddMemoryMode.LINK) }

    // Step 1: What are you saving?
    var titleInput by remember { mutableStateOf("") }
    var contentInput by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("Learn") }

    // Step 2: Why will future-you need this?
    var whyInput by remember { mutableStateOf("") }

    // Step 3: What future moment is this for?
    var selectedTriggerType by remember { mutableStateOf("When I start a project") }
    var triggerDetailInput by remember { mutableStateOf("When I start the dashboard sprint") }

    // Step 4: What is the smallest useful next step?
    var selectedActionType by remember { mutableStateOf("Focus for 20 minutes") }
    var actionDetailInput by remember { mutableStateOf("20-min layout research") }

    val categories = listOf("Learn", "Buy", "Try", "Reference", "Idea")

    // The 6 specified triggers
    val triggerChoices = listOf(
        "On a chosen date",
        "Before a deadline",
        "When I start a project",
        "During a routine",
        "When I arrive somewhere",
        "Let LATER suggest"
    )

    // The 6 specified tiny actions
    val actionChoices = listOf(
        "Read for 5 minutes",
        "Focus for 20 minutes",
        "Compare options",
        "Make a decision",
        "Add to project",
        "Open when needed"
    )

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        containerColor = LaterPaperBg,
        dragHandle = null,
        modifier = Modifier.testTag("add_memory_bottom_sheet")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .imePadding()
                .padding(horizontal = 20.dp, vertical = 18.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Header: "Save the why"
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        LaterLogoMark(
                            size = 18.dp,
                            tint = LaterTerracotta,
                            foldTint = LaterDarkAccent,
                            cutoutColor = LaterPaperBg
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "LATER INTENT ENGINE",
                            fontFamily = PlusJakartaSansFamily,
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 1.2.sp,
                            color = LaterTerracotta
                        )
                    }
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Save the why",
                        fontFamily = NewsreaderFamily,
                        fontSize = 25.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = LaterInkPrimary
                    )
                    Text(
                        text = "Bookmarks remember links. LATER remembers intentions.",
                        fontFamily = PlusJakartaSansFamily,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = LaterSecondaryText
                    )
                }

                IconButton(
                    onClick = onDismissRequest,
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = LaterTextMuted
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // User chooses: Paste a link, Add a note, Save product, Save place, Import from clipboard
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                AddMemoryMode.entries.forEach { mode ->
                    val isSelected = mode == selectedMode
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (isSelected) LaterTerracottaLight else LaterCardBg)
                            .border(
                                width = 1.dp,
                                color = if (isSelected) LaterTerracotta else LaterBorder,
                                shape = RoundedCornerShape(8.dp)
                            )
                            .clickable {
                                haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                selectedMode = mode
                                when (mode) {
                                    AddMemoryMode.LINK -> {
                                        selectedCategory = "Learn"
                                        if (titleInput.isBlank()) titleInput = "Modern CSS Grid & Subgrid Deep Dive"
                                        if (contentInput.isBlank()) contentInput = "https://ishadeed.com/article/learn-css-subgrid/"
                                    }
                                    AddMemoryMode.PRODUCT -> {
                                        selectedCategory = "Buy"
                                        selectedTriggerType = "Before a deadline"
                                        triggerDetailInput = "Before Black Friday sale window ends"
                                        selectedActionType = "Compare options"
                                        actionDetailInput = "Compare with Moonlander"
                                    }
                                    AddMemoryMode.PLACE -> {
                                        selectedCategory = "Try"
                                        selectedTriggerType = "When I arrive somewhere"
                                        triggerDetailInput = "When arriving in Bengaluru"
                                        selectedActionType = "Open when needed"
                                        actionDetailInput = "Visit for quiet work & pour-over"
                                    }
                                    AddMemoryMode.NOTE -> {
                                        selectedCategory = "Idea"
                                    }
                                    AddMemoryMode.CLIPBOARD -> {
                                        val clipText = clipboardManager.getText()?.text ?: ""
                                        if (clipText.isNotBlank()) {
                                            contentInput = clipText
                                            if (clipText.startsWith("http")) {
                                                titleInput = clipText.substringBefore("?").substringAfterLast("/")
                                                    .replace("-", " ").replace("_", " ").trim()
                                                    .ifBlank { "Saved Link" }
                                            } else {
                                                titleInput = clipText.take(45)
                                            }
                                        }
                                    }
                                }
                            }
                            .padding(horizontal = 11.dp, vertical = 7.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = mode.icon,
                            contentDescription = null,
                            tint = if (isSelected) LaterTerracotta else LaterSecondaryText,
                            modifier = Modifier.size(15.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = mode.label,
                            fontFamily = PlusJakartaSansFamily,
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSelected) LaterTerracotta else LaterInkPrimary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // =================================================================
            // STEP 1: “What are you saving?”
            // =================================================================
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(LaterCardBg)
                    .border(1.dp, LaterBorder, RoundedCornerShape(10.dp))
                    .padding(14.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(20.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(LaterTerracotta),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("1", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "WHAT ARE YOU SAVING?",
                        fontFamily = PlusJakartaSansFamily,
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 0.8.sp,
                        color = LaterInkPrimary
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Title Input
                Text(
                    text = "Title or description",
                    fontFamily = PlusJakartaSansFamily,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = LaterSecondaryText
                )
                Spacer(modifier = Modifier.height(3.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(6.dp))
                        .background(LaterPaperBg)
                        .border(1.dp, LaterBorderSubtle, RoundedCornerShape(6.dp))
                        .padding(horizontal = 12.dp, vertical = 9.dp)
                ) {
                    if (titleInput.isEmpty()) {
                        Text(
                            text = "e.g. Modern CSS Grid & Subgrid Deep Dive",
                            style = LaterTypographyTokens.searchPlaceholder
                        )
                    }
                    BasicTextField(
                        value = titleInput,
                        onValueChange = { titleInput = it },
                        textStyle = TextStyle(
                            fontFamily = PlusJakartaSansFamily,
                            fontSize = 13.5.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = LaterInkPrimary
                        ),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth().testTag("add_memory_title_field")
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Content / URL input
                Text(
                    text = "Link, reference, or note",
                    fontFamily = PlusJakartaSansFamily,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = LaterSecondaryText
                )
                Spacer(modifier = Modifier.height(3.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(6.dp))
                        .background(LaterPaperBg)
                        .border(1.dp, LaterBorderSubtle, RoundedCornerShape(6.dp))
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    if (contentInput.isEmpty()) {
                        Text(
                            text = "https://example.com or note details",
                            style = LaterTypographyTokens.searchPlaceholder.copy(fontSize = 12.sp)
                        )
                    }
                    BasicTextField(
                        value = contentInput,
                        onValueChange = { contentInput = it },
                        textStyle = TextStyle(
                            fontFamily = PlusJakartaSansFamily,
                            fontSize = 12.5.sp,
                            color = LaterSecondaryText
                        ),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth().testTag("add_memory_content_field")
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Category selection
                Text(
                    text = "Category",
                    fontFamily = PlusJakartaSansFamily,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = LaterSecondaryText
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    categories.forEach { cat ->
                        val isSelected = cat == selectedCategory
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(if (isSelected) LaterTerracottaLight else LaterPaperBg)
                                .border(
                                    width = 1.dp,
                                    color = if (isSelected) LaterTerracotta else LaterBorderSubtle,
                                    shape = RoundedCornerShape(6.dp)
                                )
                                .clickable {
                                    haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                    selectedCategory = cat
                                }
                                .padding(horizontal = 10.dp, vertical = 5.dp)
                        ) {
                            Text(
                                text = cat,
                                fontFamily = PlusJakartaSansFamily,
                                fontSize = 12.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) LaterTerracotta else LaterInkPrimary
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // =================================================================
            // STEP 2: “Why will future-you need this?”
            // =================================================================
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(LaterCardBg)
                    .border(1.dp, LaterBorder, RoundedCornerShape(10.dp))
                    .padding(14.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(20.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(LaterTerracotta),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("2", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "WHY WILL FUTURE-YOU NEED THIS?",
                        fontFamily = PlusJakartaSansFamily,
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 0.8.sp,
                        color = LaterInkPrimary
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(LaterWhyBg.copy(alpha = 0.8f))
                        .border(1.dp, LaterBorderSubtle, RoundedCornerShape(8.dp))
                        .padding(horizontal = 14.dp, vertical = 12.dp)
                ) {
                    if (whyInput.isEmpty()) {
                        Text(
                            text = "“Need this for next sprint’s dashboard redesign—subgrid may solve the card-alignment issue.”",
                            fontFamily = NewsreaderFamily,
                            fontSize = 14.5.sp,
                            lineHeight = 21.sp,
                            color = LaterTextMuted
                        )
                    }
                    BasicTextField(
                        value = whyInput,
                        onValueChange = { whyInput = it },
                        textStyle = TextStyle(
                            fontFamily = NewsreaderFamily,
                            fontSize = 15.sp,
                            lineHeight = 22.sp,
                            fontWeight = FontWeight.Medium,
                            color = LaterInkPrimary
                        ),
                        minLines = 3,
                        maxLines = 5,
                        modifier = Modifier.fillMaxWidth().testTag("add_memory_why_field")
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // =================================================================
            // STEP 3: “What future moment is this for?”
            // =================================================================
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(LaterCardBg)
                    .border(1.dp, LaterBorder, RoundedCornerShape(10.dp))
                    .padding(14.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(20.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(LaterTerracotta),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("3", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "WHAT FUTURE MOMENT IS THIS FOR?",
                        fontFamily = PlusJakartaSansFamily,
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 0.8.sp,
                        color = LaterInkPrimary
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // 6 Trigger Choices:
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    triggerChoices.forEach { trigger ->
                        val isSelected = trigger == selectedTriggerType
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(if (isSelected) LaterTerracottaLight else LaterPaperBg)
                                .border(
                                    width = 1.dp,
                                    color = if (isSelected) LaterTerracotta else LaterBorderSubtle,
                                    shape = RoundedCornerShape(6.dp)
                                )
                                .clickable {
                                    haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                    selectedTriggerType = trigger
                                    triggerDetailInput = when (trigger) {
                                        "On a chosen date" -> "On Friday morning"
                                        "Before a deadline" -> "Before the sprint planning cutoff"
                                        "When I start a project" -> "Your dashboard sprint starts today"
                                        "During a routine" -> "During Friday weekly review"
                                        "When I arrive somewhere" -> "When traveling to Bengaluru"
                                        else -> "When dashboard redesign sprint begins"
                                    }
                                }
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = trigger,
                                fontFamily = PlusJakartaSansFamily,
                                fontSize = 11.5.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) LaterTerracotta else LaterSecondaryText
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Editable Trigger Detail
                Text(
                    text = "Specific trigger context:",
                    fontFamily = PlusJakartaSansFamily,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = LaterSecondaryText
                )
                Spacer(modifier = Modifier.height(3.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(6.dp))
                        .background(LaterPaperBg)
                        .border(1.dp, LaterBorderSubtle, RoundedCornerShape(6.dp))
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    BasicTextField(
                        value = triggerDetailInput,
                        onValueChange = { triggerDetailInput = it },
                        textStyle = TextStyle(
                            fontFamily = PlusJakartaSansFamily,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = LaterInkPrimary
                        ),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth().testTag("add_memory_trigger_input")
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // =================================================================
            // STEP 4: “What is the smallest useful next step?”
            // =================================================================
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(LaterCardBg)
                    .border(1.dp, LaterBorder, RoundedCornerShape(10.dp))
                    .padding(14.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(20.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(LaterTerracotta),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("4", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "WHAT IS THE SMALLEST USEFUL NEXT STEP?",
                        fontFamily = PlusJakartaSansFamily,
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 0.8.sp,
                        color = LaterInkPrimary
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // 6 Action Choices:
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    actionChoices.forEach { actionOption ->
                        val isSelected = actionOption == selectedActionType
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(if (isSelected) LaterTerracottaLight else LaterPaperBg)
                                .border(
                                    width = 1.dp,
                                    color = if (isSelected) LaterTerracotta else LaterBorderSubtle,
                                    shape = RoundedCornerShape(6.dp)
                                )
                                .clickable {
                                    haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                    selectedActionType = actionOption
                                    actionDetailInput = when (actionOption) {
                                        "Read for 5 minutes" -> "Read for 5 minutes"
                                        "Focus for 20 minutes" -> "20-MIN LAYOUT RESEARCH"
                                        "Compare options" -> "Compare options side-by-side"
                                        "Make a decision" -> "Decide architecture path"
                                        "Add to project" -> "Attach to dashboard sprint"
                                        else -> "Open when needed"
                                    }
                                }
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = actionOption,
                                fontFamily = PlusJakartaSansFamily,
                                fontSize = 11.5.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) LaterTerracotta else LaterSecondaryText
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Editable Tiny Action Detail
                Text(
                    text = "Tiny action label:",
                    fontFamily = PlusJakartaSansFamily,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = LaterSecondaryText
                )
                Spacer(modifier = Modifier.height(3.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(6.dp))
                        .background(LaterPaperBg)
                        .border(1.dp, LaterBorderSubtle, RoundedCornerShape(6.dp))
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    BasicTextField(
                        value = actionDetailInput,
                        onValueChange = { actionDetailInput = it },
                        textStyle = TextStyle(
                            fontFamily = PlusJakartaSansFamily,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = LaterTerracotta
                        ),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth().testTag("add_memory_action_input")
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // =================================================================
            // VISIBLE "LATER MOMENT" CREATION CARD
            // WHEN [trigger], HELP ME [action]
            // =================================================================
            val finalTrigger = triggerDetailInput.ifBlank { "Your dashboard sprint starts today" }
            val finalAction = actionDetailInput.ifBlank { "Focus for 20 minutes" }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0xFFF3ECE0))
                    .border(1.dp, LaterTerracotta.copy(alpha = 0.35f), RoundedCornerShape(10.dp))
                    .padding(14.dp)
                    .testTag("created_later_moment_preview")
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = LaterTerracotta,
                            modifier = Modifier.size(15.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "LATER MOMENT",
                            fontFamily = PlusJakartaSansFamily,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 1.2.sp,
                            color = LaterTerracotta
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "WHEN $finalTrigger,",
                        fontFamily = PlusJakartaSansFamily,
                        fontSize = 13.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = LaterInkPrimary
                    )

                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        text = "HELP ME $finalAction",
                        fontFamily = PlusJakartaSansFamily,
                        fontSize = 13.5.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = LaterTerracotta
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // =================================================================
            // SAVE BUTTON: “CREATE LATER MOMENT”
            // =================================================================
            Button(
                onClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    if (!isPro && activeMomentsCount >= 3) {
                        showFreeLimitSheet = true
                    } else {
                        onSaveMemory(
                            titleInput.ifBlank { "Modern CSS Grid & Subgrid Deep Dive" },
                            whyInput.ifBlank { "Need this for next sprint’s dashboard redesign—subgrid may solve the card-alignment issue." },
                            contentInput.ifBlank { "https://ishadeed.com/article/learn-css-subgrid/" },
                            selectedCategory,
                            finalTrigger,
                            finalAction
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .shadow(4.dp, RoundedCornerShape(10.dp), ambientColor = LaterTerracotta.copy(alpha = 0.3f))
                    .testTag("create_later_moment_button"),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = LaterTerracotta,
                    contentColor = Color.White
                )
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "CREATE LATER MOMENT",
                        fontFamily = PlusJakartaSansFamily,
                        fontSize = 13.5.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                        modifier = Modifier.size(17.dp)
                    )
                }
            }
        }
    }

    if (showFreeLimitSheet) {
        FreeLimitUpgradeBottomSheet(
            onDismissRequest = { showFreeLimitSheet = false },
            onViewLaterPro = {
                showFreeLimitSheet = false
                onDismissRequest()
                onOpenPaywall()
            }
        )
    }

    if (lockedFeatureForDialog != null) {
        ProFeatureLockedDialog(
            featureName = lockedFeatureForDialog!!,
            onDismissRequest = { lockedFeatureForDialog = null },
            onUnlockPro = {
                lockedFeatureForDialog = null
                onDismissRequest()
                onOpenPaywall()
            }
        )
    }
}
