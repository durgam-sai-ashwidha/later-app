package com.example.viewmodel

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.MemoryEntity
import com.example.data.MemoryRepository
import com.example.data.RevenueCatRepository
import com.example.data.SubscriptionState
import com.example.service.AIExtractionResult
import com.example.service.AiExtractionService
import com.revenuecat.purchases.Offerings
import com.revenuecat.purchases.Package
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
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
    val revenueCatRepository: RevenueCatRepository? = null,
    private val aiService: AiExtractionService = AiExtractionService()
) : ViewModel() {

    // Demo Mode & First Launch state
    private val _isDemoMode = MutableStateFlow(false)
    val isDemoMode: StateFlow<Boolean> = _isDemoMode.asStateFlow()

    private val _hasSeenWelcome = MutableStateFlow(false)
    val hasSeenWelcome: StateFlow<Boolean> = _hasSeenWelcome.asStateFlow()

    fun completeWelcome() {
        _hasSeenWelcome.value = true
    }

    init {
        viewModelScope.launch {
            // Critical rule: A new user must never see sample CSS Grid articles, keyboards, routines,
            // or fake streaks automatically. If demo mode is not active, purge sample memories.
            if (!_isDemoMode.value) {
                repository.clearDemoWorkspace()
            }
        }
    }

    // RevenueCat Subscription State (Values: Loading, Free, Pro, Error)
    val subscriptionState: StateFlow<SubscriptionState> =
        revenueCatRepository?.subscriptionState ?: MutableStateFlow(SubscriptionState.Free).asStateFlow()

    // Observable Pro entitlement state (True only when 'pro' active entitlement exists)
    val isProSubscriber: StateFlow<Boolean> = subscriptionState.map {
        it is SubscriptionState.Pro
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = false
    )

    val currentOfferings: StateFlow<Offerings?> =
        revenueCatRepository?.currentOfferings ?: MutableStateFlow<Offerings?>(null).asStateFlow()

    // Value-based Free limit: maximum of 3 active Later Moments
    // Active means state is Ready for action, Waiting for moment, or In progress
    // (i.e. not completed and not archived)
    val activeMomentsCount: StateFlow<Int> = repository.allMemories.map { memories ->
        memories.count { !it.isCompleted && !it.isArchived }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = 0
    )

    fun isFreeLimitReached(): Boolean {
        return !isProSubscriber.value && activeMomentsCount.value >= 3
    }

    fun purchasePackage(
        activity: Activity,
        rcPackage: Package,
        onSuccess: () -> Unit,
        onCancelled: () -> Unit,
        onError: (String) -> Unit
    ) {
        revenueCatRepository?.purchasePackage(activity, rcPackage, onSuccess, onCancelled, onError)
            ?: onError("Subscription repository not initialized.")
    }

    fun restorePurchases(
        onSuccess: (Boolean) -> Unit,
        onError: (String) -> Unit
    ) {
        revenueCatRepository?.restorePurchases(onSuccess, onError)
            ?: onError("Subscription repository not initialized.")
    }

    fun refreshCustomerInfo() {
        revenueCatRepository?.fetchCustomerInfo()
        revenueCatRepository?.fetchOfferings()
    }

    fun isRevenueCatConfigured(): Boolean {
        return revenueCatRepository?.isKeyConfigured() == true
    }

    // Selected filter category for Home: null means "All"
    private val _selectedCategory = MutableStateFlow<String?>(null)
    val selectedCategory: StateFlow<String?> = _selectedCategory.asStateFlow()

    fun selectCategory(category: String?) {
        _selectedCategory.value = if (category.equals("All", ignoreCase = true)) null else category
    }

    // Selected state filter: All, Needs action, In progress, Saved for later, Completed
    private val _selectedStateFilter = MutableStateFlow("All")
    val selectedStateFilter: StateFlow<String> = _selectedStateFilter.asStateFlow()

    fun selectStateFilter(state: String) {
        _selectedStateFilter.value = state
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

    // Habit metrics: Fresh user mode starts at 0 with no fake streaks or progress
    private val _focusedActionsToday = MutableStateFlow(0)
    val focusedActionsToday: StateFlow<Int> = _focusedActionsToday.asStateFlow()

    private val _focusSessionsCompleted = MutableStateFlow(0)
    val focusSessionsCompleted: StateFlow<Int> = _focusSessionsCompleted.asStateFlow()

    private val _focusMinutesTotal = MutableStateFlow(0)
    val focusMinutesTotal: StateFlow<Int> = _focusMinutesTotal.asStateFlow()

    private val _followThroughStreak = MutableStateFlow(0)
    val followThroughStreak: StateFlow<Int> = _followThroughStreak.asStateFlow()

    private val _weeklyReflection = MutableStateFlow("")
    val weeklyReflection: StateFlow<String> = _weeklyReflection.asStateFlow()

    fun saveWeeklyReflection(text: String) {
        _weeklyReflection.value = text
    }

    fun loadDemoWorkspace(onCompleted: () -> Unit = {}) {
        viewModelScope.launch {
            _isDemoMode.value = true
            _focusedActionsToday.value = 1
            _focusSessionsCompleted.value = 2
            _focusMinutesTotal.value = 45
            _followThroughStreak.value = 3
            _weeklyReflection.value =
                "Subgrid completely eliminated our card alignment hacks in the dashboard redesign. Doing a 20-min focused session made an intimidating task easy."
            repository.loadDemoWorkspace()
            onCompleted()
        }
    }

    fun clearDemoWorkspace(onCompleted: () -> Unit = {}) {
        viewModelScope.launch {
            _isDemoMode.value = false
            _focusedActionsToday.value = 0
            _focusSessionsCompleted.value = 0
            _focusMinutesTotal.value = 0
            _followThroughStreak.value = 0
            _weeklyReflection.value = ""
            repository.clearDemoWorkspace()
            onCompleted()
        }
    }

    suspend fun getUserMemoryCount(): Int {
        return repository.getUserMemoryCount()
    }

    // Manual cycle index for "Not now" on Today's Focus
    private val _todayFocusIndex = MutableStateFlow(0)
    private val _heroDismissedToday = MutableStateFlow(false)
    val heroDismissedToday: StateFlow<Boolean> = _heroDismissedToday.asStateFlow()

    fun dismissHeroToday() {
        _heroDismissedToday.value = true
    }

    fun restoreHeroToday() {
        _heroDismissedToday.value = false
    }

    fun cycleTodayFocus() {
        _todayFocusIndex.value += 1
    }

    // All active memories
    val allActiveMemories: StateFlow<List<MemoryEntity>> = repository.allMemories.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    // Today's featured focus memory
    val todayFocusMemory: StateFlow<MemoryEntity?> = combine(
        repository.allMemories,
        _todayFocusIndex,
        _heroDismissedToday
    ) { memories, index, dismissed ->
        if (dismissed) return@combine null
        val focusEligible = memories.filter { !it.isCompleted && !it.isArchived }
        if (focusEligible.isEmpty()) {
            null
        } else {
            val candidate = focusEligible.find { it.title.contains("Grid", ignoreCase = true) }
                ?: focusEligible[index % focusEligible.size]
            candidate
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = null
    )

    // "Worth revisiting" smart memory cards (2-3 items)
    val worthRevisitingMemories: StateFlow<List<MemoryEntity>> = combine(
        repository.allMemories,
        todayFocusMemory
    ) { memories, currentFocus ->
        if (memories.isEmpty()) emptyList()
        else {
            val others = memories.filter { it.id != currentFocus?.id && !it.isCompleted && !it.isArchived }
            others.take(2)
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    // "Coming up" upcoming trigger moments
    val comingUpMemories: StateFlow<List<MemoryEntity>> = combine(
        repository.allMemories,
        todayFocusMemory,
        worthRevisitingMemories
    ) { memories, currentFocus, revisiting ->
        if (memories.isEmpty()) emptyList()
        else {
            val existingIds = setOfNotNull(currentFocus?.id) + revisiting.map { it.id }.toSet()
            val remaining = memories.filter { it.id !in existingIds && !it.isArchived && !it.isCompleted }
            remaining.take(3)
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    // Smart contextual reminders
    val smartReminders: StateFlow<List<MemoryEntity>> = repository.reminders.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    // Count of memories needing attention
    val memoriesNeedingAttentionCount: StateFlow<Int> = repository.allMemories.map { memories ->
        memories.count { it.resolveStatus() == "READY FOR ACTION" || it.resolveStatus() == "NEEDS REVIEW" || it.state == "Needs action" }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = 0
    )

    // Completed memories
    val completedMemories: StateFlow<List<MemoryEntity>> = repository.allMemories.combine(_focusedActionsToday) { memories, _ ->
        memories.filter { it.isCompleted || it.state.equals("Completed", ignoreCase = true) }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    // Filtered & sorted memories for Archive screen
    val homeMemories: StateFlow<List<MemoryEntity>> = combine(
        repository.allMemories,
        _selectedCategory,
        _selectedStateFilter,
        _searchQuery,
        _sortOption
    ) { memories, category, stateFilter, query, sort ->
        var list = memories
        if (!category.isNullOrBlank() && !category.equals("All", ignoreCase = true)) {
            list = list.filter { it.category.equals(category, ignoreCase = true) }
        }
        if (!stateFilter.equals("All", ignoreCase = true)) {
            list = list.filter { 
                when (stateFilter) {
                    "Needs action" -> it.resolveStatus() == "NEEDS ACTION" || it.state.equals("Needs action", ignoreCase = true)
                    "In progress" -> it.state.equals("In progress", ignoreCase = true) || it.resolveStatus() == "NEEDS REVIEW"
                    "Saved for later" -> it.state.equals("Saved for later", ignoreCase = true) || it.resolveStatus() == "EXPERIMENT" || it.resolveStatus() == "INSPIRATION"
                    "Completed" -> it.isCompleted || it.state.equals("Completed", ignoreCase = true)
                    else -> true
                }
            }
        }
        if (query.isNotBlank()) {
            val q = query.trim().lowercase()
            list = list.filter { item ->
                item.why.lowercase().contains(q) ||
                item.title.lowercase().contains(q) ||
                item.content.lowercase().contains(q) ||
                (item.category?.lowercase()?.contains(q) == true) ||
                (item.action?.lowercase()?.contains(q) == true) ||
                (item.projectTag?.lowercase()?.contains(q) == true)
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

    fun completeFocusSession(
        memoryId: String,
        durationMinutes: Int = 20,
        note: String? = null
    ) {
        viewModelScope.launch {
            _focusedActionsToday.value += 1
            _focusSessionsCompleted.value += 1
            _focusMinutesTotal.value += durationMinutes
            _followThroughStreak.value = (_followThroughStreak.value).coerceAtLeast(2)

            repository.updateState(memoryId, state = "Completed", isCompleted = true)

            if (!note.isNullOrBlank()) {
                val existing = repository.getByIdDirect(memoryId)
                if (existing != null) {
                    val updatedWhy = "${existing.why}\n\n[Focus Note]: $note"
                    repository.update(existing.copy(why = updatedWhy, isCompleted = true, state = "Completed"))
                }
            }
        }
    }

    fun markMemoryCompleted(memoryId: String) {
        viewModelScope.launch {
            _focusedActionsToday.value += 1
            repository.updateState(memoryId, state = "Completed", isCompleted = true)
        }
    }

    fun setMemoryState(memoryId: String, newState: String) {
        viewModelScope.launch {
            val isCompleted = newState.equals("Completed", ignoreCase = true)
            repository.updateState(memoryId, state = newState, isCompleted = isCompleted)
        }
    }

    fun snoozeReminder(memoryId: String, hours: Int = 24) {
        viewModelScope.launch {
            val newTime = System.currentTimeMillis() + (hours * 60 * 60 * 1000L)
            val memory = repository.getByIdDirect(memoryId)
            val updatedContext = memory?.reminderContext ?: "Revisit this saved resource."
            repository.updateReminder(memoryId, updatedContext, newTime)
        }
    }

    fun dismissReminder(memoryId: String) {
        viewModelScope.launch {
            repository.updateReminder(memoryId, context = null, time = null)
        }
    }

    fun updateReminder(memoryId: String, context: String?, time: Long?) {
        viewModelScope.launch {
            repository.updateReminder(memoryId, context, time)
        }
    }

    fun archiveMemory(memoryId: String) {
        viewModelScope.launch {
            repository.archive(memoryId, isArchived = true)
        }
    }

    fun saveLaterMoment(
        title: String,
        why: String,
        content: String,
        category: String,
        trigger: String,
        tinyAction: String,
        onSaved: () -> Unit
    ) {
        if (why.isBlank() && title.isBlank()) return

        val newMemory = MemoryEntity(
            title = title.ifBlank { "Untitled Memory" }.trim(),
            content = content.ifBlank { "note" }.trim(),
            why = why.trim(),
            category = category.trim(),
            trigger = trigger.trim(),
            tinyAction = tinyAction.trim(),
            reminderContext = trigger.trim(),
            reminderTime = System.currentTimeMillis() + 86400000L,
            action = if (tinyAction.endsWith("→")) tinyAction else "$tinyAction →",
            relevanceLabel = when (category.uppercase()) {
                "LEARN" -> "READY FOR ACTION"
                "BUY" -> "SALE WINDOW APPROACHING"
                "TRY" -> "READY TO VISIT"
                "REFERENCE" -> "RELATED TO CURRENT PROJECT"
                else -> "CONTEXT MATCH"
            },
            state = "Needs action",
            estimatedMinutes = if (tinyAction.contains("5 min", ignoreCase = true)) 5 else 20
        )

        viewModelScope.launch {
            repository.insert(newMemory)
            resetExtractionState()
            onSaved()
        }
    }

    fun saveProMemory(
        title: String,
        why: String,
        content: String,
        category: String,
        reminderContext: String?,
        projectTag: String?,
        estimatedMinutes: Int = 20,
        onSaved: () -> Unit
    ) {
        if (why.isBlank() && title.isBlank()) return

        val newMemory = MemoryEntity(
            title = title.ifBlank { "Untitled Memory" }.trim(),
            content = content.ifBlank { "note" }.trim(),
            why = why.trim(),
            category = category.trim(),
            reminderContext = reminderContext?.takeIf { it.isNotBlank() },
            reminderTime = if (!reminderContext.isNullOrBlank()) System.currentTimeMillis() + 86400000L else null,
            projectTag = projectTag?.takeIf { it.isNotBlank() },
            relevanceLabel = when (category.uppercase()) {
                "LEARN" -> "DUE THIS WEEK"
                "BUY" -> "SALE WINDOW APPROACHING"
                "TRY" -> "SAVED 30 DAYS AGO"
                "REFERENCE" -> "RELATED TO CURRENT PROJECT"
                else -> "CONTEXT MATCH"
            },
            state = "Needs action",
            estimatedMinutes = estimatedMinutes
        )

        viewModelScope.launch {
            repository.insert(newMemory)
            resetExtractionState()
            onSaved()
        }
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
