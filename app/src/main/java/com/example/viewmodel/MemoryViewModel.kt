package com.example.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.MemoryEntity
import com.example.data.MemoryRepository
import com.example.service.AIExtractionResult
import com.example.service.AiExtractionService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class MemorySortOption(val label: String) {
    NEWEST("Newest first"),
    OLDEST("Oldest first"),
    ACTIONABLE("Action ready first"),
    TITLE("Alphabetical")
}

sealed interface ExtractionState {
    object Idle : ExtractionState
    object Loading : ExtractionState
    data class Success(val result: AIExtractionResult) : ExtractionState
    data class Error(val message: String) : ExtractionState
}

class MemoryViewModel(
    private val repository: MemoryRepository,
    private val aiService: AiExtractionService = AiExtractionService()
) : ViewModel() {

    init {
        viewModelScope.launch {
            repository.seedSampleMemoriesIfEmpty()
        }
    }

    // RevenueCat Mock / Subscription State
    private val _isProSubscriber = MutableStateFlow(false)
    val isProSubscriber: StateFlow<Boolean> = _isProSubscriber.asStateFlow()

    fun setProSubscriber(isPro: Boolean) {
        _isProSubscriber.value = isPro
    }

    // Selected filter category for Home: null means "All"
    private val _selectedCategory = MutableStateFlow<String?>(null)
    val selectedCategory: StateFlow<String?> = _selectedCategory.asStateFlow()

    fun selectCategory(category: String?) {
        _selectedCategory.value = if (category.equals("All", ignoreCase = true)) null else category
    }

    // Sort order
    private val _sortOption = MutableStateFlow(MemorySortOption.NEWEST)
    val sortOption: StateFlow<MemorySortOption> = _sortOption.asStateFlow()

    fun setSortOption(option: MemorySortOption) {
        _sortOption.value = option
    }

    // Search query
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun clearSearch() {
        _searchQuery.value = ""
    }

    // Filtered & sorted memories for Home screen
    val homeMemories: StateFlow<List<MemoryEntity>> = combine(
        repository.allMemories,
        _selectedCategory,
        _searchQuery,
        _sortOption
    ) { memories, category, query, sort ->
        var list = memories
        if (!category.isNullOrBlank() && !category.equals("All", ignoreCase = true)) {
            list = list.filter { it.category.equals(category, ignoreCase = true) }
        }
        if (query.isNotBlank()) {
            val q = query.trim().lowercase()
            list = list.filter { item ->
                item.why.lowercase().contains(q) ||
                item.title.lowercase().contains(q) ||
                item.content.lowercase().contains(q) ||
                (item.category?.lowercase()?.contains(q) == true) ||
                (item.action?.lowercase()?.contains(q) == true)
            }
        }
        when (sort) {
            MemorySortOption.NEWEST -> list.sortedByDescending { it.createdAt }
            MemorySortOption.OLDEST -> list.sortedBy { it.createdAt }
            MemorySortOption.ACTIONABLE -> list.sortedByDescending { it.action != null }
            MemorySortOption.TITLE -> list.sortedBy { it.title.lowercase() }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    // Search results (across title, why, content, and category)
    val searchResults: StateFlow<List<MemoryEntity>> = combine(
        repository.allMemories,
        _searchQuery
    ) { memories, query ->
        if (query.isBlank()) {
            emptyList()
        } else {
            val q = query.trim().lowercase()
            memories.filter { item ->
                item.why.lowercase().contains(q) ||
                item.title.lowercase().contains(q) ||
                item.content.lowercase().contains(q) ||
                (item.category?.lowercase()?.contains(q) == true)
            }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val memoryCount: StateFlow<Int> = repository.memoryCount.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = 0
    )

    // AI Extraction State for Capture screen
    private val _extractionState = MutableStateFlow<ExtractionState>(ExtractionState.Idle)
    val extractionState: StateFlow<ExtractionState> = _extractionState.asStateFlow()

    fun extractWithAi(rawContent: String) {
        if (rawContent.isBlank()) {
            _extractionState.value = ExtractionState.Error("Please enter content or URL first")
            return
        }

        _extractionState.value = ExtractionState.Loading
        viewModelScope.launch {
            try {
                val result = aiService.extractMemory(rawContent)
                _extractionState.value = ExtractionState.Success(result)
            } catch (e: Exception) {
                _extractionState.value = ExtractionState.Error(e.localizedMessage ?: "Failed to extract")
            }
        }
    }

    fun resetExtractionState() {
        _extractionState.value = ExtractionState.Idle
    }

    fun saveMemory(
        content: String,
        title: String,
        why: String,
        category: String?,
        action: String? = null,
        onSaved: () -> Unit
    ) {
        if (why.isBlank()) return

        val newMemory = MemoryEntity(
            content = content.trim(),
            title = title.ifBlank { "Untitled Note" }.trim(),
            why = why.trim(),
            category = category?.takeIf { it.isNotBlank() },
            action = action?.takeIf { it.isNotBlank() }
        )

        viewModelScope.launch {
            repository.insert(newMemory)
            resetExtractionState()
            onSaved()
        }
    }

    fun updateMemory(memory: MemoryEntity, onUpdated: () -> Unit = {}) {
        viewModelScope.launch {
            repository.update(memory)
            onUpdated()
        }
    }

    fun deleteMemory(memory: MemoryEntity, onDeleted: () -> Unit = {}) {
        viewModelScope.launch {
            repository.delete(memory)
            onDeleted()
        }
    }

    fun deleteById(id: String, onDeleted: () -> Unit = {}) {
        viewModelScope.launch {
            repository.deleteById(id)
            onDeleted()
        }
    }
}
