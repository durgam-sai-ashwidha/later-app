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
    @Query("SELECT * FROM memories ORDER BY createdAt DESC")
    fun getAllMemories(): Flow<List<MemoryEntity>>

    @Query("""
        SELECT * FROM memories 
        WHERE title LIKE '%' || :query || '%' 
           OR why LIKE '%' || :query || '%' 
           OR content LIKE '%' || :query || '%' 
        ORDER BY createdAt DESC
    """)
    fun searchMemories(query: String): Flow<List<MemoryEntity>>

    @Query("SELECT * FROM memories WHERE category = :category ORDER BY createdAt DESC")
    fun getMemoriesByCategory(category: String): Flow<List<MemoryEntity>>

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
}
