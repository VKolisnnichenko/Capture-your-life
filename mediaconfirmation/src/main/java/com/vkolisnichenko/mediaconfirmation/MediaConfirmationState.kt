package com.vkolisnichenko.mediaconfirmation

import com.vkolisnichenko.mediaconfirmation.model.ErrorModel
import com.vkolisnichenko.mediaconfirmation.model.MediaConfirmationModel

sealed class MediaConfirmationState {
    data object Loading : MediaConfirmationState()
    data class Success(val mediaConfirmationModel: MediaConfirmationModel) : MediaConfirmationState()
    data class Error(val errorModel: ErrorModel) : MediaConfirmationState()
}