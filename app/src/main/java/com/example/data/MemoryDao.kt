package com.example.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface MemoryDao {
    @Query("SELECT * FROM memories WHERE isArchived = 0 ORDER BY createdAt DESC")
    fun getAllMemories(): Flow<List<MemoryEntity>>

    @Query("SELECT * FROM memories WHERE isArchived = 1 ORDER BY updatedAt DESC")
    fun getArchivedMemories(): Flow<List<MemoryEntity>>

    @Query("SELECT * FROM memories WHERE reminderContext IS NOT NULL AND isArchived = 0 ORDER BY createdAt DESC")
    fun getReminders(): Flow<List<MemoryEntity>>

    @Query("""
        SELECT * FROM memories 
        WHERE (title LIKE '%' || :query || '%' 
           OR why LIKE '%' || :query || '%' 
           OR content LIKE '%' || :query || '%'
           OR projectTag LIKE '%' || :query || '%')
          AND isArchived = 0
        ORDER BY createdAt DESC
    """)
    fun searchMemories(query: String): Flow<List<MemoryEntity>>

    @Query("SELECT * FROM memories WHERE category = :category AND isArchived = 0 ORDER BY createdAt DESC")
    fun getMemoriesByCategory(category: String): Flow<List<MemoryEntity>>

    @Query("SELECT * FROM memories WHERE state = :state AND isArchived = 0 ORDER BY createdAt DESC")
    fun getMemoriesByState(state: String): Flow<List<MemoryEntity>>

    @Query("UPDATE memories SET state = :state, isCompleted = :isCompleted, updatedAt = :updatedAt WHERE id = :id")
    suspend fun updateMemoryState(id: String, state: String, isCompleted: Boolean, updatedAt: Long = System.currentTimeMillis())

    @Query("UPDATE memories SET reminderContext = :context, reminderTime = :time, updatedAt = :updatedAt WHERE id = :id")
    suspend fun updateMemoryReminder(id: String, context: String?, time: Long?, updatedAt: Long = System.currentTimeMillis())

    @Query("UPDATE memories SET isArchived = :isArchived, updatedAt = :updatedAt WHERE id = :id")
    suspend fun updateArchiveStatus(id: String, isArchived: Boolean, updatedAt: Long = System.currentTimeMillis())

    @Query("SELECT * FROM memories WHERE id = :id LIMIT 1")
    fun getMemoryById(id: String): Flow<MemoryEntity?>

    @Query("SELECT * FROM memories WHERE id = :id LIMIT 1")
    suspend fun getMemoryByIdDirect(id: String): MemoryEntity?

    @Query("SELECT COUNT(*) FROM memories")
    fun getMemoryCount(): Flow<Int>

    @Query("SELECT COUNT(*) FROM memories")
    suspend fun getMemoryCountDirect(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(memory: MemoryEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(memories: List<MemoryEntity>)

    @Update
    suspend fun update(memory: MemoryEntity)

    @Delete
    suspend fun delete(memory: MemoryEntity)

    @Query("DELETE FROM memories WHERE id = :id")
    suspend fun deleteById(id: String)

    @Query("DELETE FROM memories WHERE id LIKE 'sample-%'")
    suspend fun deleteDemoMemories()

    @Query("DELETE FROM memories")
    suspend fun deleteAllMemories()

    @Query("SELECT COUNT(*) FROM memories WHERE id NOT LIKE 'sample-%'")
    suspend fun getUserMemoryCountDirect(): Int

    @Query("SELECT COUNT(*) FROM memories WHERE id LIKE 'sample-%'")
    suspend fun getDemoMemoryCountDirect(): Int
}
