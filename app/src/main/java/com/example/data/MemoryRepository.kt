package com.example.data

import kotlinx.coroutines.flow.Flow

class MemoryRepository(private val dao: MemoryDao) {
    val allMemories: Flow<List<MemoryEntity>> = dao.getAllMemories()
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

    suspend fun delete(memory: MemoryEntity) {
        dao.delete(memory)
    }

    suspend fun deleteById(id: String) {
        dao.deleteById(id)
    }

    suspend fun seedSampleMemoriesIfEmpty() {
        val count = dao.getMemoryCountDirect()
        if (count == 0) {
            dao.insertAll(SampleMemories.items)
        }
    }
}
