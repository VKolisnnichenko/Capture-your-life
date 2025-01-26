package com.vkolisnichenko.mediaconfirmation.usecase

import com.vkolisnichenko.core.Either
import com.vkolisnichenko.core.domain.repository.MediaCaptureRepository
import com.vkolisnichenko.core.domain.usecases.base.BaseUseCase
import javax.inject.Inject

class RemoveMediaUseCase @Inject constructor(
    private val mediaCaptureRepository: MediaCaptureRepository
) : BaseUseCase<Unit, Long>() {
    override suspend fun run(params: Long) =
        Either.success(mediaCaptureRepository.deleteMediaCaptureById(params))
}