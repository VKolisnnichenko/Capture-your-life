package com.vkolisnichenko.core.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.vkolisnichenko.core.domain.entity.MemoryCaptureEntity

@Dao
interface MemoryCaptureDao {
    @Insert
    fun insertMemoryCapture(memoryCaptureEntity: MemoryCaptureEntity)

    @Query("UPDATE MemoryCaptureEntity SET user_notes = :userNotes WHERE id = :id")
    fun updateMemoryCaptureById(id: Long, userNotes: String)

    @Query("SELECT * FROM MemoryCaptureEntity")
    fun getAllMemoryCaptures(): List<MemoryCaptureEntity>

    @Query("SELECT * FROM MemoryCaptureEntity WHERE id = :id")
    fun getMemoryCaptureById(id: Long): MemoryCaptureEntity

    @Query("DELETE FROM MemoryCaptureEntity WHERE id = :id")
    fun deleteMemoryCaptureById(id: Long)
}