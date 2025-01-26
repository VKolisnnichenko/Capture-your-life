package com.vkolisnichenko.core.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.vkolisnichenko.core.data.database.dao.MemoryCaptureDao
import com.vkolisnichenko.core.domain.entity.MemoryCaptureEntity

@Database(entities = [MemoryCaptureEntity::class], version = 1)
@TypeConverters(UriTypeConverter::class)
abstract class DataBase : RoomDatabase() {
    abstract fun memoryCaptureDao(): MemoryCaptureDao
}
