package com.vkolisnichenko.core.data.di

import com.vkolisnichenko.core.data.database.dao.MemoryCaptureDao
import com.vkolisnichenko.core.data.repository.MediaCaptureRepositoryImpl
import com.vkolisnichenko.core.data.repository.MediaRepositoryImpl
import com.vkolisnichenko.core.domain.repository.MediaCaptureRepository
import com.vkolisnichenko.core.domain.repository.MediaRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun provideMediaRepository(): MediaRepository {
        return MediaRepositoryImpl()
    }

    @Singleton
    @Provides
    fun provideMediaCaptureRepository(memoryCaptureDao: MemoryCaptureDao): MediaCaptureRepository {
        return MediaCaptureRepositoryImpl(memoryCaptureDao)
    }

}
