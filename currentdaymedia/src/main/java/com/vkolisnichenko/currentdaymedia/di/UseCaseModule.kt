package com.vkolisnichenko.currentdaymedia.di

import com.vkolisnichenko.core.domain.repository.MediaCaptureRepository
import com.vkolisnichenko.currentdaymedia.usecase.GetCurrentWeekDayUseCase
import com.vkolisnichenko.currentdaymedia.usecase.GetSpecificDayRecordsUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {
    @Singleton
    @Provides
    fun provideGetCurrentDayUseCase(
    ): GetCurrentWeekDayUseCase {
        return GetCurrentWeekDayUseCase()
    }

    @Singleton
    @Provides
    fun provideGetSpecificDayRecordsUseCase(
        mediaCaptureRepository: MediaCaptureRepository
    ): GetSpecificDayRecordsUseCase {
        return GetSpecificDayRecordsUseCase(mediaCaptureRepository)
    }
}