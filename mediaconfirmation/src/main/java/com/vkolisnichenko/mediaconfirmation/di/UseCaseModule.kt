package com.vkolisnichenko.mediaconfirmation.di

import com.vkolisnichenko.core.domain.repository.MediaCaptureRepository
import com.vkolisnichenko.core.domain.repository.MediaRepository
import com.vkolisnichenko.mediaconfirmation.usecase.GetCaptureMediaUseCase
import com.vkolisnichenko.mediaconfirmation.usecase.GetCurrentMediaUseCase
import com.vkolisnichenko.mediaconfirmation.usecase.InsertMediaCaptureUseCase
import com.vkolisnichenko.mediaconfirmation.usecase.RemoveMediaUseCase
import com.vkolisnichenko.mediaconfirmation.usecase.UpdateMediaUseCase
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
    fun provideGetCurrentMediaUseCase(
        mediaRepository: MediaRepository
    ): GetCurrentMediaUseCase {
        return GetCurrentMediaUseCase(mediaRepository)
    }

    @Singleton
    @Provides
    fun provideInsertMediaCaptureUseCase(
        mediaCaptureRepository: MediaCaptureRepository
    ): InsertMediaCaptureUseCase {
        return InsertMediaCaptureUseCase(mediaCaptureRepository)
    }

    @Singleton
    @Provides
    fun provideGetCaptureMediaUseCase(
        mediaCaptureRepository: MediaCaptureRepository
    ): GetCaptureMediaUseCase {
        return GetCaptureMediaUseCase(mediaCaptureRepository)
    }

    @Singleton
    @Provides
    fun provideRemoveMediaUseCase(
        mediaCaptureRepository: MediaCaptureRepository
    ): RemoveMediaUseCase {
        return RemoveMediaUseCase(mediaCaptureRepository)
    }

    @Singleton
    @Provides
    fun provideUpdateMediaUseCase(
        mediaCaptureRepository: MediaCaptureRepository
    ): UpdateMediaUseCase {
        return UpdateMediaUseCase(mediaCaptureRepository)
    }
}
