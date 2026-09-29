package com.example

import com.example.data.MemoryEntity
import com.example.data.SampleMemories
import com.example.ui.components.formatArchiveRelativeTime
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {
    @Test
    fun sampleMemories_areValidAndNonEmpty() {
        val samples = SampleMemories.items
        assertEquals(4, samples.size)
        samples.forEach { memory ->
            assertNotNull(memory.id)
            assertTrue("Title must not be blank", memory.title.isNotBlank())
            assertTrue("WHY must not be blank", memory.why.isNotBlank())
            assertTrue("Content must not be blank", memory.content.isNotBlank())
            assertNotNull(memory.category)
        }
    }

    @Test
    fun memoryEntity_creationDefaultValues() {
        val memory = MemoryEntity(
            content = "https://example.com/ai",
            title = "Gemini Flash AI",
            why = "Need for real-time extraction benchmark next Monday.",
            category = "Learn"
        )
        assertNotNull(memory.id)
        assertTrue(memory.createdAt > 0)
        assertEquals("Learn", memory.category)
        assertEquals("Need for real-time extraction benchmark next Monday.", memory.why)
    }

    @Test
    fun relativeTime_formatsCorrectly() {
        val now = System.currentTimeMillis()
        assertEquals("just now", formatArchiveRelativeTime(now))
        assertEquals("5 minutes ago", formatArchiveRelativeTime(now - 5 * 60 * 1000L))
        assertEquals("2 hours ago", formatArchiveRelativeTime(now - 2 * 60 * 60 * 1000L))
        assertEquals("yesterday", formatArchiveRelativeTime(now - 1 * 24 * 60 * 60 * 1000L))
        assertEquals("2 days ago", formatArchiveRelativeTime(now - 2 * 24 * 60 * 60 * 1000L))
    }

    @Test
    fun freeLimit_threeActiveMomentsCalculated() {
        val activeMemories = listOf(
            MemoryEntity(title = "Task 1", why = "Why 1", content = "C1", isCompleted = false, isArchived = false),
            MemoryEntity(title = "Task 2", why = "Why 2", content = "C2", isCompleted = false, isArchived = false),
            MemoryEntity(title = "Task 3", why = "Why 3", content = "C3", isCompleted = false, isArchived = false),
            MemoryEntity(title = "Task 4", why = "Why 4", content = "C4", isCompleted = true, isArchived = false),
            MemoryEntity(title = "Task 5", why = "Why 5", content = "C5", isCompleted = false, isArchived = true)
        )
        val activeCount = activeMemories.count { !it.isCompleted && !it.isArchived }
        assertEquals(3, activeCount)
        val isLimitReachedForFree = activeCount >= 3
        assertTrue("Free users reach limit at 3 active memories", isLimitReachedForFree)
    }
}
