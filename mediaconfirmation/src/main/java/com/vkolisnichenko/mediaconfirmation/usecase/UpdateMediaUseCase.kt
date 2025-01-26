package com.vkolisnichenko.mediaconfirmation.usecase

import com.vkolisnichenko.core.Either
import com.vkolisnichenko.core.domain.repository.MediaCaptureRepository
import com.vkolisnichenko.core.domain.usecases.base.BaseUseCase
import com.vkolisnichenko.mediaconfirmation.model.UpdateMediaModel

class UpdateMediaUseCase(private val mediaCaptureRepository: MediaCaptureRepository) :
    BaseUseCase<Unit, UpdateMediaModel>() {

    override suspend fun run(params: UpdateMediaModel) =
        Either.success(mediaCaptureRepository.updateMediaCapture(params.mediaId, params.userNotes))
}