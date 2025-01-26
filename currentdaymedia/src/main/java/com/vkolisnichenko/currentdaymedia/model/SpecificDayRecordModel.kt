package com.vkolisnichenko.currentdaymedia.model

import android.net.Uri

data class SpecificDayRecordModel(
    val mediaId : Long = -1,
    val isPhoto: Boolean = false,
    val mediaUri: Uri = Uri.EMPTY,
    val currentDate: String = "",
    var userNotes: String = "",
)