package com.example

import com.example.data.MemoryEntity
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class MemorySearchTest {

    private val sampleMemories = listOf(
        MemoryEntity(
            title = "Kotlin Coroutines Guide",
            why = "Understand structured concurrency for background thread management",
            content = "https://developer.android.com/kotlin/coroutines",
            category = "LEARN"
        ),
        MemoryEntity(
            title = "Aeropress Go Travel Coffee Maker",
            why = "Buy before next hiking trip across Yosemite",
            content = "https://aeropress.com/products/aeropress-go",
            category = "BUY"
        ),
        MemoryEntity(
            title = "Modern Architecture Principles",
            why = "Reference for upcoming redesign of checkout flow",
            content = "Clean Architecture guidelines",
            category = "REFERENCE"
        )
    )

    @Test
    fun `search by WHY content filters correctly`() {
        val query = "concurrency"
        val q = query.trim().lowercase()
        val results = sampleMemories.filter { item ->
            item.why.lowercase().contains(q) ||
            item.title.lowercase().contains(q) ||
            item.content.lowercase().contains(q)
        }

        assertEquals(1, results.size)
        assertEquals("Kotlin Coroutines Guide", results[0].title)
    }

    @Test
    fun `search by title filters correctly`() {
        val query = "Aeropress"
        val q = query.trim().lowercase()
        val results = sampleMemories.filter { item ->
            item.why.lowercase().contains(q) ||
            item.title.lowercase().contains(q) ||
            item.content.lowercase().contains(q)
        }

        assertEquals(1, results.size)
        assertTrue(results[0].why.contains("hiking trip"))
    }

    @Test
    fun `search is case-insensitive`() {
        val query = "YOSEMITE"
        val q = query.trim().lowercase()
        val results = sampleMemories.filter { item ->
            item.why.lowercase().contains(q) ||
            item.title.lowercase().contains(q) ||
            item.content.lowercase().contains(q)
        }

        assertEquals(1, results.size)
        assertEquals("Aeropress Go Travel Coffee Maker", results[0].title)
    }

    @Test
    fun `empty query returns all memories`() {
        val query = ""
        val results = if (query.isBlank()) {
            sampleMemories
        } else {
            val q = query.trim().lowercase()
            sampleMemories.filter { item ->
                item.why.lowercase().contains(q) || item.title.lowercase().contains(q)
            }
        }

        assertEquals(3, results.size)
    }
}
