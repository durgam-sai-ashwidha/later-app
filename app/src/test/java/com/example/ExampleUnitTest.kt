package com.example

import com.example.data.MemoryEntity
import com.example.data.SampleMemories
import com.example.ui.components.formatRelativeTime
import com.example.util.DateUtils
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {
    @Test
    fun sampleMemories_areValidAndNonEmpty() {
        val samples = SampleMemories.items
        assertEquals(3, samples.size)
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
        assertEquals("Just now", DateUtils.formatRelativeDate(now, now))
        assertEquals("5 minutes ago", DateUtils.formatRelativeDate(now - 5 * 60 * 1000, now))
        assertEquals("2 hours ago", DateUtils.formatRelativeDate(now - 2 * 60 * 60 * 1000, now))
        assertEquals("Yesterday", DateUtils.formatRelativeDate(now - 1 * 24 * 60 * 60 * 1000, now))
        assertEquals("2 days ago", DateUtils.formatRelativeDate(now - 2 * 24 * 60 * 60 * 1000, now))
        assertEquals("Yesterday", formatRelativeTime(now - 1 * 24 * 60 * 60 * 1000))
    }
}
