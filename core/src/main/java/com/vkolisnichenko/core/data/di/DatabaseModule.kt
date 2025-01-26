package com.vkolisnichenko.core.data.di

import android.content.Context
import androidx.room.Room
import com.vkolisnichenko.core.data.database.DataBase
import com.vkolisnichenko.core.data.database.dao.MemoryCaptureDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): DataBase {
        return Room.databaseBuilder(
            context,
            DataBase::class.java,
            "database.db"
        ).build()
    }

    @Singleton
    @Provides
    fun provideMemoryCaptureDao(database: DataBase): MemoryCaptureDao {
        return database.memoryCaptureDao()
    }
}
