package com.vkolisnichenko.currentdaymedia.usecase

import com.vkolisnichenko.core.Either
import com.vkolisnichenko.core.domain.usecases.base.BaseUseCase
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

class GetCurrentWeekDayUseCase : BaseUseCase<String, String>() {
    override suspend fun run(params: String) = Either.success(getDayOfWeek(params))

    private fun getDayOfWeek(dateString: String): String {
        val formatter = DateTimeFormatter.ofPattern("MMMM d, yyyy", Locale.ENGLISH)
        val date = LocalDate.parse(dateString, formatter)
        return date.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.ENGLISH)
    }
}