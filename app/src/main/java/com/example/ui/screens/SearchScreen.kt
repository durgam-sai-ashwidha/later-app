package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.components.MemoryCard
import com.example.ui.theme.LaterBorder
import com.example.ui.theme.LaterBorderSubtle
import com.example.ui.theme.LaterCardBg
import com.example.ui.theme.LaterInkPrimary
import com.example.ui.theme.LaterPaperBg
import com.example.ui.theme.LaterTerracotta
import com.example.ui.theme.LaterWarmGray
import com.example.ui.theme.LaterTextMuted
import com.example.ui.theme.NewsreaderFamily
import com.example.ui.theme.PlusJakartaSansFamily
import com.example.viewmodel.MemoryViewModel

@Composable
fun SearchScreen(
    viewModel: MemoryViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToDetail: (String) -> Unit
) {
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val searchResults by viewModel.searchResults.collectAsStateWithLifecycle()

    var activeCategoryFilter by remember { mutableStateOf<String?>(null) }

    val filteredResults = remember(searchResults, activeCategoryFilter) {
        if (activeCategoryFilter.isNullOrBlank()) {
            searchResults
        } else {
            searchResults.filter { it.category.equals(activeCategoryFilter, ignoreCase = true) }
        }
    }

    val categories = listOf("ALL", "LEARN", "BUY", "TRY", "REFERENCE", "IDEA")

    Scaffold(
        containerColor = LaterPaperBg
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Archival Search Bar Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onNavigateBack,
                    modifier = Modifier.testTag("search_back_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = LaterInkPrimary,
                        modifier = Modifier.size(22.dp)
                    )
                }

                Spacer(modifier = Modifier.width(6.dp))

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .shadow(1.dp, RoundedCornerShape(8.dp), ambientColor = LaterInkPrimary.copy(alpha = 0.04f))
                        .clip(RoundedCornerShape(8.dp))
                        .background(LaterCardBg)
                        .border(1.dp, LaterBorder, RoundedCornerShape(8.dp))
                        .padding(horizontal = 14.dp, vertical = 12.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            tint = LaterWarmGray,
                            modifier = Modifier.size(18.dp)
                        )

                        Spacer(modifier = Modifier.width(10.dp))

                        Box(modifier = Modifier.weight(1f)) {
                            if (searchQuery.isEmpty()) {
                                Text(
                                    text = "Search reasons, words, or sources…",
                                    fontFamily = PlusJakartaSansFamily,
                                    fontSize = 14.sp,
                                    color = LaterTextMuted
                                )
                            }
                            BasicTextField(
                                value = searchQuery,
                                onValueChange = { viewModel.setSearchQuery(it) },
                                textStyle = TextStyle(
                                    fontFamily = PlusJakartaSansFamily,
                                    fontSize = 15.sp,
                                    color = LaterInkPrimary
                                ),
                                singleLine = true,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("search_text_field")
                            )
                        }

                        if (searchQuery.isNotEmpty()) {
                            IconButton(
                                onClick = { viewModel.setSearchQuery("") },
                                modifier = Modifier
                                    .size(24.dp)
                                    .testTag("clear_search_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Clear,
                                    contentDescription = "Clear",
                                    tint = LaterWarmGray,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }
                }
            }

            // Category Filter Strip
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(18.dp)
            ) {
                categories.forEach { category ->
                    val isSelected = if (category == "ALL") {
                        activeCategoryFilter == null
                    } else {
                        activeCategoryFilter.equals(category, ignoreCase = true)
                    }

                    Column(
                        modifier = Modifier
                            .clickable {
                                activeCategoryFilter = if (category == "ALL") null else category
                            }
                            .padding(vertical = 4.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = category,
                            fontFamily = PlusJakartaSansFamily,
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            letterSpacing = 1.sp,
                            color = if (isSelected) LaterTerracotta else LaterWarmGray
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Box(
                            modifier = Modifier
                                .width(if (isSelected) 20.dp else 0.dp)
                                .height(2.5.dp)
                                .clip(RoundedCornerShape(1.dp))
                                .background(if (isSelected) LaterTerracotta else androidx.compose.ui.graphics.Color.Transparent)
                        )
                    }
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(LaterBorderSubtle)
            )

            // Results List
            if (filteredResults.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 36.dp, vertical = 70.dp),
                    contentAlignment = Alignment.TopCenter
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = if (searchQuery.isBlank()) "SEARCH YOUR ARCHIVE" else "NO MATCHING MEMORIES",
                            fontFamily = PlusJakartaSansFamily,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.3.sp,
                            color = LaterInkPrimary
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = if (searchQuery.isBlank()) {
                                "Search for why you saved something, not just where it came from."
                            } else {
                                "Try searching for the intent, keywords, or the problem you wanted to solve."
                            },
                            fontFamily = NewsreaderFamily,
                            fontSize = 15.sp,
                            lineHeight = 22.sp,
                            color = LaterWarmGray,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .testTag("search_results_list"),
                    contentPadding = PaddingValues(horizontal = 20.dp, vertical = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(filteredResults, key = { it.id }) { memory ->
                        MemoryCard(
                            memory = memory,
                            onClick = { onNavigateToDetail(memory.id) },
                            onDelete = { viewModel.deleteMemory(memory) }
                        )
                    }
                }
            }
        }
    }
}
