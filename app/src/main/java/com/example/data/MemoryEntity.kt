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
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
) {
    /**
     * Resolves an actionable status tag (e.g. “NEEDS ACTION”, “NEEDS REVIEW”).
     */
    fun resolveStatus(): String {
        if (!status.isNullOrBlank()) return status.trim().uppercase()
        val cat = category?.uppercase() ?: ""
        val lowerWhy = why.lowercase()
        val lowerTitle = title.lowercase()

        return when {
            lowerWhy.contains("dashboard") || lowerTitle.contains("grid") || cat == "LEARN" ->
                "NEEDS ACTION"
            lowerWhy.contains("compare") || lowerWhy.contains("keyboard") || cat == "BUY" ->
                "NEEDS REVIEW"
            cat == "TRY" ->
                "EXPERIMENT"
            cat == "IDEA" ->
                "INCUBATING"
            cat == "REFERENCE" || lowerWhy.contains("hackathon") ->
                "INSPIRATION"
            else ->
                "NEEDS ACTION"
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
            lowerWhy.contains("prototype") || lowerWhy.contains("experiment") || cat == "TRY" ->
                "TEST PROTOTYPE →"
            cat == "IDEA" ->
                "DRAFT OUTLINE →"
            cat == "REFERENCE" || lowerWhy.contains("hackathon") || lowerWhy.contains("sync") ->
                "EXPLORE ARCHITECTURE →"
            cat == "LEARN" ->
                "START: REVIEW LESSON →"
            else ->
                "ACT ON THIS →"
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
