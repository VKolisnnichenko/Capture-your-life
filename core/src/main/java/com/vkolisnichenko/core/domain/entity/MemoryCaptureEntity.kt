package com.vkolisnichenko.core.domain.entity

import android.net.Uri
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class MemoryCaptureEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "time") val time: String,
    @ColumnInfo(name = "media") val media: Uri,
    @ColumnInfo(name = "user_notes") val userNotes: String
)