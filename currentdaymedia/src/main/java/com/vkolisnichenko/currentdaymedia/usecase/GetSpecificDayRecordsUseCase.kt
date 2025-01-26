package com.vkolisnichenko.currentdaymedia.usecase

import android.util.Log
import com.vkolisnichenko.core.Either
import com.vkolisnichenko.core.domain.entity.MemoryCaptureEntity
import com.vkolisnichenko.core.domain.repository.MediaCaptureRepository
import com.vkolisnichenko.core.domain.usecases.base.BaseUseCase
import com.vkolisnichenko.core.presentation.Mapper
import com.vkolisnichenko.currentdaymedia.model.SpecificDayRecordModel
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject

class GetSpecificDayRecordsUseCase @Inject constructor(
    private val mediaCaptureRepository: MediaCaptureRepository
) : BaseUseCase<List<SpecificDayRecordModel>, String>(),
    Mapper<MemoryCaptureEntity, SpecificDayRecordModel> {
    override suspend fun run(params: String): Either<List<SpecificDayRecordModel>> {
        val sortedList = mediaCaptureRepository.getAllMediaCaptures().filter {
            it.time.contains(convertDate(params))
        }
        return Either.success(sortedList.map { map(it) })
    }

    private fun convertDate(inputDate: String): String {
        val inputFormat = SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH)
        val outputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)

        val date = inputFormat.parse(inputDate)
        return date?.let { outputFormat.format(date) }
            ?: throw IllegalArgumentException("Invalid date format")
    }

    private fun extractTimeWithoutSeconds(dateTime: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val outputFormat = SimpleDateFormat("MMMM d, HH:mm", Locale.getDefault())

        val date = inputFormat.parse(dateTime)
        return date?.let { outputFormat.format(it) }
            ?: throw IllegalArgumentException("Invalid date format")
    }

    override fun map(input: MemoryCaptureEntity) =
        SpecificDayRecordModel(
            mediaId = input.id,
            isPhoto = input.media.toString().contains("jpg") || input.media.toString().contains("png"),
            mediaUri = input.media,
            currentDate = extractTimeWithoutSeconds(input.time),
            userNotes = input.userNotes
        )
}