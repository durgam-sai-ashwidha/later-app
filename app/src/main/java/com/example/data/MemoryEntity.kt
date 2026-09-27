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
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)
