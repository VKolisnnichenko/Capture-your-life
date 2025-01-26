package com.vkolisnichenko.mediaconfirmation.usecase

import com.vkolisnichenko.core.Either
import com.vkolisnichenko.core.domain.entity.MemoryCaptureEntity
import com.vkolisnichenko.core.domain.repository.MediaCaptureRepository
import com.vkolisnichenko.core.domain.usecases.base.BaseUseCase
import com.vkolisnichenko.core.presentation.Mapper
import com.vkolisnichenko.mediaconfirmation.model.MediaConfirmationModel
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject

class GetCaptureMediaUseCase @Inject constructor(
    private val mediaCaptureRepository: MediaCaptureRepository
) : BaseUseCase<MediaConfirmationModel, Long>(),
    Mapper<MemoryCaptureEntity, MediaConfirmationModel> {
    override suspend fun run(params: Long) =
        Either.success(map(mediaCaptureRepository.getMediaCaptureById(params)))

    override fun map(input: MemoryCaptureEntity) =
        MediaConfirmationModel(
            isPhoto = input.media.toString().contains("jpg") || input.media.toString().contains("png"),
            mediaUri = input.media,
            currentDate = extractTimeWithoutSeconds(input.time),
            userNotes = input.userNotes
        )

    private fun extractTimeWithoutSeconds(dateTime: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val outputFormat = SimpleDateFormat("MMMM d, HH:mm", Locale.getDefault())

        val date = inputFormat.parse(dateTime)
        return date?.let { outputFormat.format(it) }
            ?: throw IllegalArgumentException("Invalid date format")
    }


}