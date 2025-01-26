package com.vkolisnichenko.currentdaymedia.presentation

import com.vkolisnichenko.currentdaymedia.model.CurrentDayModel

sealed class CurrentDayMediaState {
    data object Loading : CurrentDayMediaState()
    data class Success(val currentDayModel: CurrentDayModel) : CurrentDayMediaState()
    data class Error(val message: String) : CurrentDayMediaState()
}