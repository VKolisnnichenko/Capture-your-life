package com.vkolisnichenko.mediaconfirmation.usecase

import com.vkolisnichenko.core.Either
import com.vkolisnichenko.core.domain.entity.MemoryCaptureEntity
import com.vkolisnichenko.core.domain.repository.MediaCaptureRepository
import com.vkolisnichenko.core.domain.usecases.base.BaseUseCase
import com.vkolisnichenko.core.presentation.Mapper
import com.vkolisnichenko.mediaconfirmation.model.MediaConfirmationModel
import javax.inject.Inject

class InsertMediaCaptureUseCase @Inject constructor(
    private val mediaCaptureRepository: MediaCaptureRepository
) : BaseUseCase<Unit, MediaConfirmationModel>(),
    Mapper<MediaConfirmationModel, MemoryCaptureEntity> {

    override suspend fun run(params: MediaConfirmationModel) =
        Either.success(mediaCaptureRepository.insertMediaCapture(map(params)))

    override fun map(input: MediaConfirmationModel) =
        MemoryCaptureEntity(
            time = input.currentDate,
            media = input.mediaUri,
            userNotes = input.userNotes
        )
}