package com.vkolisnichenko.mediaconfirmation.model

import android.net.Uri


data class MediaConfirmationModel(
     val isPhoto : Boolean = false,
     val mediaUri : Uri = Uri.EMPTY,
     val currentDate : String = "",
     var userNotes : String = "",
     var onFinishScreen : Boolean = false
)