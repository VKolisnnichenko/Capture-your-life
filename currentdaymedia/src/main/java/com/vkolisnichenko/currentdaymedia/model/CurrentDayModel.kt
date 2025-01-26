package com.vkolisnichenko.currentdaymedia.model

data class CurrentDayModel(
    var currentDayOfTheWeek: String = "",
    val currentDayRecords: List<SpecificDayRecordModel> = emptyList()
)