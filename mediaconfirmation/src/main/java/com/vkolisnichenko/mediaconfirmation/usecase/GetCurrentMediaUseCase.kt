package com.vkolisnichenko.mediaconfirmation.usecase

import com.vkolisnichenko.core.Either
import com.vkolisnichenko.core.domain.model.MediaModel
import com.vkolisnichenko.core.domain.repository.MediaRepository
import com.vkolisnichenko.core.domain.usecases.base.BaseUseCase
import javax.inject.Inject

class GetCurrentMediaUseCase @Inject constructor(
    private val mediaRepository: MediaRepository
) : BaseUseCase<MediaModel, Unit>() {
    override suspend fun run(params: Unit) = Either.success(mediaRepository.getMedia())
}