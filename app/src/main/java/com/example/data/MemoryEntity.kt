package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "memories")
data class MemoryEntity(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val content: String,
    val title: String,
    val why: String,
    val category: String? = null,
    val action: String? = null,
    val status: String? = null,
    val relevanceLabel: String? = null,
    val reminderContext: String? = null,
    val reminderTime: Long? = null,
    val projectTag: String? = null,
    val trigger: String? = null,
    val tinyAction: String? = null,
    val state: String = "Needs action", // Needs action, In progress, Saved for later, Completed
    val isArchived: Boolean = false,
    val isCompleted: Boolean = false,
    val estimatedMinutes: Int = 20,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
) {
    /**
     * Resolves the future moment / trigger context (e.g. "Your dashboard sprint starts today").
     */
    fun resolveTrigger(): String {
        if (!trigger.isNullOrBlank()) return trigger.trim()
        if (!reminderContext.isNullOrBlank()) return reminderContext.trim()
        val lowerWhy = why.lowercase()
        val lowerTitle = title.lowercase()
        return when {
            lowerWhy.contains("dashboard") || lowerTitle.contains("grid") ->
                "Your dashboard sprint starts today"
            lowerWhy.contains("keyboard") || lowerWhy.contains("compare") ->
                "Black Friday sale window approaches"
            lowerWhy.contains("bengaluru") || lowerWhy.contains("roastery") ->
                "When traveling to Bengaluru"
            lowerWhy.contains("sync") || lowerWhy.contains("hackathon") ->
                "Sync protocol design starts on Thursday"
            lowerWhy.contains("calm") || lowerWhy.contains("manifesto") ->
                "When drafting the design essay"
            else ->
                "When you start this project"
        }
    }

    /**
     * Resolves the smallest useful next step / tiny action (e.g. "20-MIN LAYOUT RESEARCH").
     */
    fun resolveTinyAction(): String {
        if (!tinyAction.isNullOrBlank()) return tinyAction.trim()
        val lowerWhy = why.lowercase()
        val lowerTitle = title.lowercase()
        return when {
            lowerWhy.contains("dashboard") || lowerTitle.contains("grid") ->
                "20-min layout research"
            lowerWhy.contains("keyboard") || lowerWhy.contains("compare") ->
                "Compare with Moonlander"
            lowerWhy.contains("bengaluru") || lowerWhy.contains("roastery") ->
                "Visit for quiet work & pour-over"
            lowerWhy.contains("sync") || lowerWhy.contains("hackathon") ->
                "Review offline-first patterns"
            lowerWhy.contains("calm") || lowerWhy.contains("manifesto") ->
                "Draft outline for 15 minutes"
            else ->
                action?.removeSuffix("→")?.trim() ?: "Focus for 20 minutes"
        }
    }

    /**
     * Constructs the canonical Later Moment equation:
     * WHEN [trigger], HELP ME [action]
     */
    fun resolveLaterMoment(): String {
        return "WHEN ${resolveTrigger()}, HELP ME ${resolveTinyAction()}"
    }

    /**
     * Resolves an actionable status tag (e.g. “LEARN · READY FOR ACTION”, “NEEDS ACTION”).
     */
    fun resolveStatus(): String {
        if (!status.isNullOrBlank()) return status.trim().uppercase()
        val cat = category?.uppercase() ?: ""
        val lowerWhy = why.lowercase()
        val lowerTitle = title.lowercase()

        return when {
            lowerWhy.contains("dashboard") || lowerTitle.contains("grid") || cat == "LEARN" ->
                "READY FOR ACTION"
            lowerWhy.contains("compare") || lowerWhy.contains("keyboard") || cat == "BUY" ->
                "NEEDS REVIEW"
            cat == "TRY" ->
                "READY TO VISIT"
            cat == "IDEA" ->
                "INCUBATING"
            cat == "REFERENCE" || lowerWhy.contains("hackathon") ->
                "ACTIVE REFERENCE"
            else ->
                "READY FOR ACTION"
        }
    }

    /**
     * Resolves a clear, specific, capitalized primary next action with arrow,
     * fulfilling: “START: REDESIGN DASHBOARD →” or “COMPARE OPTIONS →”
     */
    fun resolveAction(): String {
        val customAction = action?.trim()
        if (!customAction.isNullOrBlank()) {
            return if (customAction.endsWith("→")) customAction else "$customAction →"
        }
        val cat = category?.uppercase() ?: ""
        val lowerWhy = why.lowercase()
        val lowerTitle = title.lowercase()

        return when {
            lowerWhy.contains("dashboard") || lowerWhy.contains("subgrid") || lowerTitle.contains("grid") ->
                "START: REDESIGN DASHBOARD →"
            lowerWhy.contains("compare") || lowerWhy.contains("keyboard") || cat == "BUY" ->
                "COMPARE OPTIONS →"
            lowerWhy.contains("restaurant") || lowerWhy.contains("coffee") || cat == "TRY" ->
                "VIEW ON MAP & MENU →"
            lowerWhy.contains("prototype") || lowerWhy.contains("experiment") ->
                "TEST PROTOTYPE →"
            cat == "IDEA" ->
                "DRAFT OUTLINE →"
            cat == "REFERENCE" || lowerWhy.contains("hackathon") || lowerWhy.contains("sync") ->
                "EXPLORE ARCHITECTURE →"
            cat == "LEARN" ->
                "START: REDESIGN DASHBOARD →"
            else ->
                "START FOLLOW-THROUGH →"
        }
    }

    /**
     * Formats raw URLs into clean, readable domains (e.g. "ishadeed.com"),
     * avoiding long raw URLs on the memory cards.
     */
    fun cleanSourceDomain(): String {
        val trimmed = content.trim()
        if (trimmed.startsWith("http://") || trimmed.startsWith("https://")) {
            val domain = trimmed
                .removePrefix("https://")
                .removePrefix("http://")
                .removePrefix("www.")
                .substringBefore("/")
                .substringBefore("?")
            if (domain.isNotBlank()) return domain
        }
        return if (trimmed.contains(".") && !trimmed.contains(" ") && trimmed.length < 35) {
            trimmed
        } else if (trimmed.length > 35) {
            "saved resource"
        } else {
            trimmed
        }
    }

    /**
     * Human-readable relative time for editorial source row (e.g. "3h ago", "Yesterday").
     */
    fun formatRelativeTime(): String {
        val now = System.currentTimeMillis()
        val diff = (now - createdAt).coerceAtLeast(0)
        val hours = diff / (1000 * 60 * 60)
        val days = diff / (1000 * 60 * 60 * 24)
        return when {
            hours < 1 -> "just now"
            hours in 1..23 -> "${hours}h ago"
            days == 1L -> "Yesterday"
            days in 2..6 -> "${days}d ago"
            else -> "${days / 7}w ago"
        }
    }
}
