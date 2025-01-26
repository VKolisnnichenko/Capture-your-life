package com.vkolisnichenko.mainscreen.domain.usecase

import com.vkolisnichenko.core.Either
import com.vkolisnichenko.core.domain.entity.MemoryCaptureEntity
import com.vkolisnichenko.core.domain.model.MediaModel
import com.vkolisnichenko.core.domain.repository.MediaCaptureRepository
import com.vkolisnichenko.core.domain.usecases.base.BaseUseCase
import com.vkolisnichenko.core.presentation.Mapper
import com.vkolisnichenko.mainscreen.domain.model.DayModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.time.format.TextStyle
import java.util.Locale
import javax.inject.Inject

class FetchAllDataUseCase @Inject constructor(
    private val mediaCaptureRepository: MediaCaptureRepository
) : BaseUseCase<List<DayModel>, Unit>(), Mapper<List<MemoryCaptureEntity>, List<DayModel>> {

    override suspend fun run(params: Unit): Either<List<DayModel>> {
        return Either.success(map(mediaCaptureRepository.getAllMediaCaptures()))
    }

    override fun map(input: List<MemoryCaptureEntity>): List<DayModel> {
        val groupedData = input.groupBy {
            parseToDate(it.time)
        }

        return groupedData.map { (dayOfWeek, entries) ->
            val media = entries.map {
                MediaModel(
                    id = it.id,
                    mediaUri = it.media,
                    isPhoto = it.media.toString().contains(".jpg")
                            || it.media.toString().contains(".png")
                )
            }
            val date = parseToDay(entries[0].time)
            DayModel(date, dayOfWeek, media)
        }
    }

    private fun parseToDay(time: String): String {
        try {
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            val date = LocalDateTime.parse(time, formatter).toLocalDate()
            return date.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault())
        } catch (e: Exception) {
            e.printStackTrace()
            throw e
        }
    }

    private fun parseToDate(time: String): String {
        if (time.isBlank()) return ""
        val formatterWithTime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val outputFormatter =
            DateTimeFormatter.ofPattern("MMMM d, yyyy", Locale.getDefault())

        return try {
            val trimmedTime = time.trim()
            val date = try {
                LocalDateTime.parse(trimmedTime, formatterWithTime).toLocalDate()
            } catch (e: DateTimeParseException) {
                LocalDateTime.parse("$trimmedTime 0 G0:00:00", formatterWithTime).toLocalDate()
            }
            date.format(outputFormatter)
        } catch (e: Exception) {
            ""
        }
    }

}
