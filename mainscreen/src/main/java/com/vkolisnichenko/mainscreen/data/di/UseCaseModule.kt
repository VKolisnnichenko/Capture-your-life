package com.vkolisnichenko.mainscreen.data.di

import com.vkolisnichenko.core.domain.repository.MediaCaptureRepository
import com.vkolisnichenko.core.domain.repository.MediaRepository
import com.vkolisnichenko.mainscreen.domain.usecase.FetchAllDataUseCase
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
    fun provideFetchDataMediaUseCase(
        mediaCaptureRepository: MediaCaptureRepository
    ): FetchAllDataUseCase {
        return FetchAllDataUseCase(mediaCaptureRepository)
    }
}