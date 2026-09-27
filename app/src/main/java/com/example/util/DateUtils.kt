package com.example.util

/**
 * Utility functions for calculating human-readable relative dates.
 */
object DateUtils {

    /**
     * Formats a millisecond timestamp into a human-readable relative date string
     * (e.g., 'Just now', '2 hours ago', 'Yesterday', '3 days ago').
     */
    fun formatRelativeDate(timestamp: Long, now: Long = System.currentTimeMillis()): String {
        val diff = (now - timestamp).coerceAtLeast(0)
        val seconds = diff / 1000
        val minutes = seconds / 60
        val hours = minutes / 60
        val days = hours / 24
        val weeks = days / 7
        val months = days / 30
        val years = days / 365

        return when {
            seconds < 60 -> "Just now"
            minutes == 1L -> "1 minute ago"
            minutes < 60 -> "$minutes minutes ago"
            hours == 1L -> "1 hour ago"
            hours < 24 -> "$hours hours ago"
            days == 1L -> "Yesterday"
            days < 7 -> "$days days ago"
            weeks == 1L -> "1 week ago"
            weeks < 5 -> "$weeks weeks ago"
            months == 1L -> "1 month ago"
            months < 12 -> "$months months ago"
            years == 1L -> "1 year ago"
            else -> "$years years ago"
        }
    }
}

/**
 * Top-level convenience function to calculate human-readable relative dates.
 */
fun formatRelativeDate(timestamp: Long, now: Long = System.currentTimeMillis()): String {
    return DateUtils.formatRelativeDate(timestamp, now)
}
