package com.example.ui.screens

import android.content.Context
import android.content.Intent
import android.net.Uri
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.AlertDialog
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.components.BookmarkCorner
import com.example.ui.components.formatArchiveRelativeTime
import com.example.ui.theme.LaterBorder
import com.example.ui.theme.LaterBorderSubtle
import com.example.ui.theme.LaterCardBg
import com.example.ui.theme.LaterInkPrimary
import com.example.ui.theme.LaterPaperBg
import com.example.ui.theme.LaterTerracotta
import com.example.ui.theme.LaterTextMuted
import com.example.ui.theme.LaterTypographyTokens
import com.example.ui.theme.LaterWarmGray
import com.example.ui.theme.LaterWhyBg
import com.example.ui.theme.NewsreaderFamily
import com.example.ui.theme.PlusJakartaSansFamily
import com.example.viewmodel.MemoryViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * 15. Detail Screen:
 * Opening a memory feels like opening a personal archive entry.
 * Hierarchy:
 * 1. WHY YOU SAVED THIS (Large WHY text)
 * 2. TITLE
 * 3. CATEGORY · DATE
 * 4. SOURCE
 * Actions: Open Resource, Edit, Delete
 */
@Composable
fun MemoryDetailScreen(
    memoryId: String,
    viewModel: MemoryViewModel,
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    val allMemories by viewModel.homeMemories.collectAsStateWithLifecycle()
    val memory = allMemories.find { it.id == memoryId }

    var showDeleteDialog by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf(false) }

    if (memory == null) {
        Scaffold(containerColor = LaterPaperBg) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Memory entry not found in archive.",
                    fontFamily = PlusJakartaSansFamily,
                    fontSize = 15.sp,
                    color = LaterWarmGray
                )
            }
        }
        return
    }

    val isUrl = remember(memory.content) {
        memory.content.startsWith("http://", ignoreCase = true) ||
        memory.content.startsWith("https://", ignoreCase = true)
    }

    val formattedDate = remember(memory.createdAt) {
        val sdf = SimpleDateFormat("MMMM d, yyyy", Locale.getDefault())
        sdf.format(Date(memory.createdAt))
    }

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
                    .widthIn(max = 680.dp)
                    .padding(horizontal = 22.dp, vertical = 18.dp)
            ) {
                // Header Bar: Back & Share
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = onNavigateBack,
                        modifier = Modifier.testTag("detail_back_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back to Archive",
                            tint = LaterInkPrimary,
                            modifier = Modifier.size(22.dp)
                        )
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(
                            onClick = {
                                val sendIntent = Intent().apply {
                                    action = Intent.ACTION_SEND
                                    putExtra(
                                        Intent.EXTRA_TEXT,
                                        "Why I saved this: “${memory.why}”\n\n${memory.title}\n${memory.content}\n\nSaved with LATER"
                                    )
                                    type = "text/plain"
                                }
                                context.startActivity(Intent.createChooser(sendIntent, "Share Memory"))
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Share,
                                contentDescription = "Share",
                                tint = LaterInkPrimary,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        IconButton(
                            onClick = { showEditDialog = true },
                            modifier = Modifier.testTag("detail_edit_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Edit Memory",
                                tint = LaterInkPrimary,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        IconButton(
                            onClick = { showDeleteDialog = true },
                            modifier = Modifier.testTag("detail_delete_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Delete Memory",
                                tint = LaterTerracotta,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Main Archival Entry Container: restrained 6dp corners, subtle border, spacious padding
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(1.dp, RoundedCornerShape(6.dp), ambientColor = LaterInkPrimary.copy(alpha = 0.03f))
                        .clip(RoundedCornerShape(6.dp))
                        .background(LaterCardBg)
                        .border(1.dp, LaterBorder, RoundedCornerShape(6.dp))
                        .padding(26.dp)
                ) {
                    BookmarkCorner(
                        modifier = Modifier.align(Alignment.TopEnd),
                        size = 18.dp,
                        color = LaterTerracotta
                    )

                    Column(modifier = Modifier.fillMaxWidth()) {
                        // 1. TOP: "WHY YOU SAVED THIS" & Large WHY text (token-enforced)
                        Text(
                            text = "WHY YOU SAVED THIS",
                            style = LaterTypographyTokens.whyHeaderLabel
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        // Dominant Editorial WHY Callout
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(6.dp))
                                .background(LaterWhyBg.copy(alpha = 0.6f))
                                .border(1.dp, LaterTerracotta.copy(alpha = 0.2f), RoundedCornerShape(6.dp))
                                .padding(20.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.Top
                            ) {
                                Text(
                                    text = "“",
                                    style = LaterTypographyTokens.whyQuoteMarkHero,
                                    modifier = Modifier.padding(end = 8.dp)
                                )

                                Text(
                                    text = memory.why,
                                    style = LaterTypographyTokens.whyHero
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        // Hairline divider
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(1.dp)
                                .background(LaterBorderSubtle)
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        // 2. TITLE
                        Text(
                            text = memory.title,
                            style = LaterTypographyTokens.titleDetail
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // 3. CATEGORY · DATE
                        val categoryStr = (memory.category ?: "ARCHIVE").uppercase()
                        Text(
                            text = "$categoryStr · ${formattedDate.uppercase()}",
                            style = LaterTypographyTokens.metaCategoryTime
                        )

                        Spacer(modifier = Modifier.height(22.dp))

                        // 4. SOURCE
                        Text(
                            text = "ORIGINAL SOURCE & CONTENT",
                            fontFamily = PlusJakartaSansFamily,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.3.sp,
                            color = LaterTextMuted
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(6.dp))
                                .background(LaterPaperBg)
                                .border(1.dp, LaterBorder, RoundedCornerShape(6.dp))
                                .padding(16.dp)
                        ) {
                            Text(
                                text = memory.content,
                                fontFamily = PlusJakartaSansFamily,
                                fontSize = 13.5.sp,
                                color = LaterInkPrimary,
                                lineHeight = 20.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(28.dp))

                        // Actions: Open Resource, Edit, Delete
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            if (isUrl) {
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(LaterInkPrimary)
                                        .clickable {
                                            try {
                                                val url = if (!memory.content.startsWith("http")) {
                                                    "https://${memory.content}"
                                                } else memory.content
                                                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                                                context.startActivity(browserIntent)
                                            } catch (e: Exception) {
                                                Toast.makeText(context, "Could not open link", Toast.LENGTH_SHORT).show()
                                            }
                                        }
                                        .padding(vertical = 12.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = "Open Resource",
                                            fontFamily = PlusJakartaSansFamily,
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Icon(
                                            imageVector = Icons.AutoMirrored.Filled.OpenInNew,
                                            contentDescription = null,
                                            tint = Color.White,
                                            modifier = Modifier.size(15.dp)
                                        )
                                    }
                                }
                            }

                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(6.dp))
                                    .border(1.dp, LaterBorder, RoundedCornerShape(6.dp))
                                    .background(LaterCardBg)
                                    .clickable { showEditDialog = true }
                                    .padding(vertical = 12.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "Edit Memory",
                                    fontFamily = PlusJakartaSansFamily,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = LaterInkPrimary
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    // Edit Memory Dialog
    if (showEditDialog) {
        var editWhy by remember { mutableStateOf(memory.why) }
        var editTitle by remember { mutableStateOf(memory.title) }

        AlertDialog(
            onDismissRequest = { showEditDialog = false },
            containerColor = LaterCardBg,
            shape = RoundedCornerShape(8.dp),
            title = {
                Text(
                    text = "EDIT MEMORY",
                    fontFamily = PlusJakartaSansFamily,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 1.4.sp,
                    color = LaterInkPrimary
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(
                        text = "WHY YOU SAVED THIS",
                        fontFamily = PlusJakartaSansFamily,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = LaterTerracotta
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(6.dp))
                            .background(LaterPaperBg)
                            .border(1.dp, LaterBorder, RoundedCornerShape(6.dp))
                            .padding(12.dp)
                    ) {
                        BasicTextField(
                            value = editWhy,
                            onValueChange = { editWhy = it },
                            textStyle = TextStyle(
                                fontFamily = NewsreaderFamily,
                                fontSize = 17.sp,
                                color = LaterInkPrimary,
                                lineHeight = 24.sp
                            ),
                            modifier = Modifier.fillMaxWidth().height(80.dp)
                        )
                    }

                    Text(
                        text = "TITLE",
                        fontFamily = PlusJakartaSansFamily,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = LaterWarmGray
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(6.dp))
                            .background(LaterPaperBg)
                            .border(1.dp, LaterBorder, RoundedCornerShape(6.dp))
                            .padding(12.dp)
                    ) {
                        BasicTextField(
                            value = editTitle,
                            onValueChange = { editTitle = it },
                            textStyle = TextStyle(
                                fontFamily = PlusJakartaSansFamily,
                                fontSize = 14.sp,
                                color = LaterInkPrimary
                            ),
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (editWhy.isNotBlank()) {
                            viewModel.updateMemory(
                                memory.copy(
                                    why = editWhy.trim(),
                                    title = editTitle.ifBlank { memory.title }.trim()
                                )
                            )
                            showEditDialog = false
                            Toast.makeText(context, "Memory updated.", Toast.LENGTH_SHORT).show()
                        }
                    }
                ) {
                    Text(
                        "Save Changes",
                        fontFamily = PlusJakartaSansFamily,
                        fontWeight = FontWeight.Bold,
                        color = LaterTerracotta
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = { showEditDialog = false }) {
                    Text("Cancel", fontFamily = PlusJakartaSansFamily, color = LaterWarmGray)
                }
            }
        )
    }

    // Delete Memory Dialog
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            containerColor = LaterCardBg,
            shape = RoundedCornerShape(8.dp),
            title = {
                Text(
                    text = "Delete Memory?",
                    fontFamily = PlusJakartaSansFamily,
                    fontWeight = FontWeight.Bold,
                    color = LaterInkPrimary
                )
            },
            text = {
                Text(
                    text = "This memory will be permanently removed from your personal archive.",
                    fontFamily = PlusJakartaSansFamily,
                    fontSize = 14.sp,
                    color = LaterWarmGray
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDeleteDialog = false
                        viewModel.deleteMemory(memory)
                        onNavigateBack()
                    }
                ) {
                    Text(
                        "Delete",
                        fontFamily = PlusJakartaSansFamily,
                        fontWeight = FontWeight.Bold,
                        color = LaterTerracotta
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text(
                        "Cancel",
                        fontFamily = PlusJakartaSansFamily,
                        color = LaterWarmGray
                    )
                }
            }
        )
    }
}
