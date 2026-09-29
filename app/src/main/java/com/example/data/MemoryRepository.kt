package com.example.data

import kotlinx.coroutines.flow.Flow

class MemoryRepository(private val dao: MemoryDao) {
    val allMemories: Flow<List<MemoryEntity>> = dao.getAllMemories()
    val archivedMemories: Flow<List<MemoryEntity>> = dao.getArchivedMemories()
    val reminders: Flow<List<MemoryEntity>> = dao.getReminders()
    val memoryCount: Flow<Int> = dao.getMemoryCount()

    fun search(query: String): Flow<List<MemoryEntity>> {
        return if (query.isBlank()) {
            dao.getAllMemories()
        } else {
            dao.searchMemories(query.trim())
        }
    }

    fun getByCategory(category: String): Flow<List<MemoryEntity>> {
        return dao.getMemoriesByCategory(category)
    }

    fun getByState(state: String): Flow<List<MemoryEntity>> {
        return dao.getMemoriesByState(state)
    }

    fun getById(id: String): Flow<MemoryEntity?> {
        return dao.getMemoryById(id)
    }

    suspend fun getByIdDirect(id: String): MemoryEntity? {
        return dao.getMemoryByIdDirect(id)
    }

    suspend fun insert(memory: MemoryEntity) {
        dao.insert(memory)
    }

    suspend fun update(memory: MemoryEntity) {
        dao.update(memory.copy(updatedAt = System.currentTimeMillis()))
    }

    suspend fun updateState(id: String, state: String, isCompleted: Boolean) {
        dao.updateMemoryState(id, state, isCompleted)
    }

    suspend fun updateReminder(id: String, context: String?, time: Long?) {
        dao.updateMemoryReminder(id, context, time)
    }

    suspend fun archive(id: String, isArchived: Boolean = true) {
        dao.updateArchiveStatus(id, isArchived)
    }

    suspend fun delete(memory: MemoryEntity) {
        dao.delete(memory)
    }

    suspend fun deleteById(id: String) {
        dao.deleteById(id)
    }

    suspend fun loadDemoWorkspace() {
        dao.insertAll(SampleMemories.items)
    }

    suspend fun clearDemoWorkspace() {
        dao.deleteDemoMemories()
    }

    suspend fun clearAllMemories() {
        dao.deleteAllMemories()
    }

    suspend fun getUserMemoryCount(): Int {
        return dao.getUserMemoryCountDirect()
    }

    suspend fun getDemoMemoryCount(): Int {
        return dao.getDemoMemoryCountDirect()
    }
}
